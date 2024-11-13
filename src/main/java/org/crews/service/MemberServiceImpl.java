package org.crews.service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.MemberRequest;
import org.crews.dto.MemberResponse;
import org.crews.dto.core.AccountResponseDto;
import org.crews.dto.core.MemberToCoreDto;
import org.crews.jwt.JWTUtil;
import org.crews.model.Member;
import org.crews.model.RefreshEntity;
import org.crews.repository.MemberRepository;
import org.crews.repository.RefreshRepository;
import org.crews.utils.AESUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final RefreshRepository refreshRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final AESUtil aesUtil;

//    private final MemberRepository memberRepository;
//    private final CoreService coreService;
//
//    public List<AccountResponseDto> getAccountInfoFromCore(Long id) {
//        Member member = memberRepository
//                .findById(id).orElseThrow(() -> new IllegalStateException("해당 회원을을 찾을 수 없습니다."));
//        log.info("{} : {}",member.getName(),member.getPhoneNumber());
//        return coreService.findCoreSideAccounts(MemberToCoreDto.fromEntity(member));
//    }

    @Override
    public MemberResponse signUp(MemberRequest memberRequest) {
        try {
            // 이메일 중복 확인
            boolean isExist = memberRepository.existsByEmail(aesUtil.encrypt(memberRequest.getEmail()));
            log.info(String.valueOf(isExist));
            if (isExist) {
                return null;
            }

            // 회원 정보 설정
            Member member = Member.from(memberRequest);
            member.setEmail(aesUtil.encrypt(member.getEmail()));
            member.setName(aesUtil.encrypt(member.getName()));
            member.setPhoneNumber(aesUtil.encrypt(member.getPhoneNumber()));
            member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));

            // 회원 저장
            Member savedMember = memberRepository.save(member);
            return MemberResponse.from(savedMember);

        } catch (Exception e) {
            throw new IllegalStateException("Failed to access the database", e);
        }
    }

    @Override
    public ResponseEntity refreshCheck(String refresh) {
        String tokenName = "refresh";
        if (refresh == null) {

            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals(tokenName)) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }
        //DB에 저장되어 있는지 확인
        boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        return null;
    }

    @Override
    public Map<String, String> reissueTokens(String refresh) {
        String accessTokenName = "access";
        String refreshTokenName = "refresh";
        Map<String, String> tokens = new HashMap<>();

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccessToken = jwtUtil.createJwt(accessTokenName, username, role, 600000L);
        String newRefreshToken = jwtUtil.createJwt(refreshTokenName, username, role, 86400000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);

        Date date = new Date(System.currentTimeMillis() + 86400000L);

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setUsername(username);
        refreshEntity.setRefresh(newRefreshToken);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);

        tokens.put(accessTokenName, newAccessToken);
        tokens.put(refreshTokenName, newRefreshToken);

        return tokens;
    }

}
