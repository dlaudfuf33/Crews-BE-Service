package org.crews.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.AccountIssuedResponse;
import org.crews.dto.core.AccountResponse;
import org.crews.dto.core.CIRequest;
import org.crews.dto.core.MemberToCoreRequest;
import org.crews.dto.request.EmailRequest;
import org.crews.dto.request.MemberRequest;
import org.crews.dto.response.InterestingResponse;
import org.crews.dto.response.MemberResponse;
import org.crews.dto.response.MyProfileResponse;
import org.crews.dto.response.MyinfoResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.jwt.JWTUtil;
import org.crews.model.*;
import org.crews.repository.AccountRepository;
import org.crews.repository.BankRepository;
import org.crews.repository.MemberRepository;
import org.crews.repository.RefreshRepository;
import org.crews.utils.AESUtil;
import org.crews.utils.CIGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private static final Random RANDOM = new Random();

    private final MemberRepository memberRepository;
    private final RefreshRepository refreshRepository;
    private final BankRepository bankRepository;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final AESUtil aesUtil;
    private final CoreService coreService;


    @Override
    @Transactional
    public MemberResponse signUp(MemberRequest memberRequest) {
        try {
            // 이메일 중복 확인
            boolean isExist = memberRepository.existsByEmail(aesUtil.encrypt(memberRequest.getEmail()));
            log.info(String.valueOf(isExist));
            if (isExist) {
                throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
            }

            // 회원 정보 설정
            Member member = Member.from(memberRequest);
            member.setEmail(aesUtil.encrypt(member.getEmail()));
            member.setName(aesUtil.encrypt(member.getName()));
            member.setPhoneNumber(aesUtil.encrypt(member.getPhoneNumber()));
            member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
            String jumin = createNumber(13, "");
            String ci = CIGenerator.generateCI(jumin);
            member.setCi(ci);
            // 회원 저장
            Member savedMember = memberRepository.save(member);

            CIRequest ciRequest = CIRequest.builder().name(memberRequest.getName())
                    .email(memberRequest.getEmail()).phone(memberRequest.getPhoneNumber()).ci(ci).build();
            Mono<AccountIssuedResponse> stringMono = coreService.sendCICode(ciRequest);
            AccountIssuedResponse response = stringMono.block();
            if(response == null)
                throw new CustomException(ErrorCode.CI_CODE_SEND_ERROR);
            Bank bank = bankRepository.findByBankCode(response.getBankCode()).orElseThrow(
                () -> new CustomException(ErrorCode.WRONG_BANKCODE)
            );
            Account account = Account.builder().bank(bank).member(member).maskedAccountNumber(maskedAccountNumber(response.getAccountNumber()))
                .accountNumber(AESUtil.encrypt(response.getAccountNumber())).balance(response.getBalance()).
                accountType(response.getAccountType()).fintecNumber(response.getFintechUseNum()).build();
            accountRepository.save(account);
            return MemberResponse.from(savedMember);

        } catch (Exception e) {
            throw new CustomException(ErrorCode.DATABASE_ACCESS_FAILED, e);
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
        Long memberId = jwtUtil.getMemberId(refresh);

        String newAccessToken = jwtUtil.createJwt(accessTokenName, username, role, memberId, 600000L);
        String newRefreshToken = jwtUtil.createJwt(refreshTokenName, username, role, memberId, 86400000L);

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

    @Override
    public List<AccountResponse> getAccountInfoFromCore(Long id) {
        Member member = memberRepository
                .findById(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        log.info("{} : {}", member.getName(), member.getPhoneNumber());
        return coreService.findCoreSideAccounts(MemberToCoreRequest.from(member));
    }

    private String createNumber(int count, String prefix) {
        StringBuilder randomNum = new StringBuilder();
        randomNum.append(prefix);
        for (int i = 0; i < count; i++) {
            int createNum = RANDOM.nextInt(10); // 0~9 사이의 랜덤 숫자 생성
            randomNum.append(createNum);
        }
        return randomNum.toString();
    }

    @Override
    public MyProfileResponse getMyProfile(String memberEmail) {
        return MyProfileResponse.from(
                memberRepository
                        .findByEmailWithInterestings(memberEmail)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)));

    }

    @Override
    public MyinfoResponse getMyinfo(String memberEmail) {
        return MyinfoResponse.from(
                memberRepository
                        .findByEmailWithAddresses(memberEmail)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)));
    }

    @Override
    public List<InterestingResponse> getMyInterests(String memberEmail) {
        return memberRepository
                .findByEmailWithInterestings(memberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERESTS_NOT_FOUND))
                .getMemberAndInterestings().stream()
                .map(MemberAndInteresting::getInteresting)
                .map(InterestingResponse::from)
                .toList();
    }

    @Override
    public boolean validateEmail(EmailRequest request) {
        return memberRepository.existsByEmail(aesUtil.encrypt(request.getEmail()));
    }

    private String maskedAccountNumber(String accountNumber) {
        String maskingResult = "";

        if (accountNumber.length() >= 7) {
            maskingResult = accountNumber.replaceAll("(?<=.{4}).(?=.{2})", "*");
        } else {
            maskingResult = accountNumber;
        }

        return maskingResult;
    }

}
