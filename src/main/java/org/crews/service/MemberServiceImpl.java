package org.crews.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.AccountIssuedResponse;
import org.crews.dto.core.AccountResponse;
import org.crews.dto.core.CIRequest;
import org.crews.dto.core.MemberToCoreRequest;
import org.crews.dto.request.*;
import org.crews.dto.response.*;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.jwt.JWTUtil;
import org.crews.model.*;
import org.crews.repository.*;
import org.crews.utils.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private static final Random RANDOM = new Random();

    private final MemberRepository memberRepository;
    private final RefreshRepository refreshRepository;
    private final MemberAndInterestingRepository memberAndInterestingRepository;
    private final BankRepository bankRepository;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;
    private final AESUtil aesUtil;
    private final CoreService coreService;
    private final InterestingRepository interestingRepository;
    private final AddressService addressService;
    private final AuthUtil authUtil;
    private final MessageRepository messageRepository;



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
            // 렌덤 닉네임 설정
            member.setNickName(NicknameGenerator.generateRandomNickname());
            // 주소 처리
            Address address = addressService.findOrCreateAddress(
                    memberRequest.getAddressDo(),
                    memberRequest.getAddressSi(),
                    memberRequest.getAddressGuGun(),
                    memberRequest.getAddressDong()
            );
            member.setAddress(address);
            // 회원 저장
            Member savedMember = memberRepository.save(member);

            // 회원 관심사 설정 (기본 값)
            memberAndInterestingRepository.save(MemberAndInteresting.builder()
                    .member(savedMember)
                    .interesting(interestingRepository.findById(1L).orElseThrow())
                    .build());


            CIRequest ciRequest = CIRequest.builder().name(memberRequest.getName())
                    .email(memberRequest.getEmail()).phone(memberRequest.getPhoneNumber()).ci(ci).build();
            Mono<AccountIssuedResponse> stringMono = coreService.sendCICode(ciRequest);
            AccountIssuedResponse response = stringMono.block();
            if (response == null)
                throw new CustomException(ErrorCode.CI_CODE_SEND_ERROR);
            Bank bank = bankRepository.findByBankCode(response.getBankCode()).orElseThrow(
                    () -> new CustomException(ErrorCode.WRONG_BANKCODE)
            );
            Account account = Account.builder().bank(bank).member(member).maskedAccountNumber(maskedAccountNumber(response.getAccountNumber()))
                    .accountNumber(AESUtil.encrypt(response.getAccountNumber())).balance(response.getBalance()).
                    accountType(response.getAccountType()).fintecNumber(response.getFintechUseNum()).productName(response.getProductName()).build();
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
            return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals(tokenName)) {

            //response status code
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        //DB에 저장되어 있는지 확인
        boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //response body
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
    public MyProfileResponse getMyProfile(Long memberId) {
        return MyProfileResponse.from(
                memberRepository
                        .findByIdWithInterestings(memberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)));

    }

    @Override
    public MyinfoResponse getMyinfo(Long memberId) {
        return MyinfoResponse.from(
                memberRepository
                        .findByIdWithAddresses(memberId)
                        .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)));
    }

    @Override
    public List<InterestResponse> getMyInterests(Long memberId) {
        return memberRepository
                .findByIdWithInterestings(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INTERESTS_NOT_FOUND))
                .getMemberAndInterestings().stream()
                .map(MemberAndInteresting::getInteresting)
                .map(InterestResponse::from)
                .toList();
    }

    @Override
    public boolean validateEmail(EmailRequest request) {
        return memberRepository.existsByEmail(aesUtil.encrypt(request.getEmail()));
    }

    @Override
    public MyNicknameResponse getMyNickname(Long memberId) {
        return MyNicknameResponse.from(memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND))
        );
    }

    @Override
    @Transactional
    public MyNicknameResponse updateMyNickname(Long memberId, MyNicknameRequest myNicknameRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.setNickName(myNicknameRequest.getNickname());
        Member savedMember = memberRepository.saveAndFlush(member);
        return MyNicknameResponse.from(savedMember);
    }

    @Override
    @Transactional
    public void updateMyInterestings(Long memberId, InterestsUpdateRequest interestsUpdateRequest) {
        memberAndInterestingRepository.deleteByMemberIdCustom(memberId);
        Member foundMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        interestsUpdateRequest.getInterests().forEach(item -> {
            MemberAndInteresting memberAndInteresting = new MemberAndInteresting();
            memberAndInteresting.setMember(foundMember);
            memberAndInteresting.setInteresting(interestingRepository.findById(item.getInterestId())
                    .orElseThrow(() -> new CustomException(ErrorCode.INTERESTS_NOT_FOUND)));
            memberAndInterestingRepository.save(memberAndInteresting);
        });
    }

    @Override
    public AddressResponse getMyAddresses(Long memberId) {
        return AddressResponse.from(memberRepository.findByWithAddress(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        ).getAddress());
    }

    @Override
    @Transactional
    public void updateMyAddresses(Long memberId, AddressRequest addressRequest) {
        Member member = memberRepository.findByWithAddress(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.setAddress(addressService.findOrCreateAddress(
                addressRequest.getDoName(),
                addressRequest.getSiName(),
                addressRequest.getGuName(),
                addressRequest.getDongName()
        ));
        memberRepository.save(member);
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

    @Override
    public FindMemberIdResponse findMemberId(FindMemberRequest findMemberRequest) {

        Member member = memberRepository.findByNameAndPhoneNumber(AESUtil.encrypt(findMemberRequest.getName()), AESUtil.encrypt(findMemberRequest.getPhoneNumber()))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return FindMemberIdResponse.from(member);
    }

    @Override
    @Transactional
    public void findMemberPw(FindMemberPwRequest findMemberPwRequest) {
        Member member = memberRepository.findByEmailAndNameAndPhoneNumber(AESUtil.encrypt(findMemberPwRequest.getEmail()), AESUtil.encrypt(findMemberPwRequest.getName()), AESUtil.encrypt(findMemberPwRequest.getPhoneNumber()))
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        String temporary = authUtil.generateRandomPassword(10);
        member.setPassword(bCryptPasswordEncoder.encode(temporary));

        MessageUtil.send(AESUtil.decrypt(member.getPhoneNumber()),temporary);
    }

    @Override
    @Transactional
    public void getVerifyNumber(VerifyPhoneRequest verifyPhoneRequest){
        String verifyNumber = authUtil.verifyRandomNumber();

        Message message = messageRepository.findByPhoneNumber(verifyPhoneRequest.getPhoneNumber())
                .orElse(new Message());
        message.setPhoneNumber(verifyPhoneRequest.getPhoneNumber());
        message.setVerifyNumber(verifyNumber);

        messageRepository.save(message);

        MessageUtil.send(verifyPhoneRequest.getPhoneNumber(),verifyNumber);
    }

    @Override
    @Transactional
    public void verifyNumberCheck(VerifyNumberRequest verifyNumberRequest){
        Message message = messageRepository.findByPhoneNumber(verifyNumberRequest.getPhoneNumber())
                .orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));

        if(Duration.between(message.getUpdatedAt(), LocalDateTime.now()).toMinutes() > 3) throw new CustomException(ErrorCode.VERIFY_NUMBER_EXPIRED);

        if (!message.getVerifyNumber().equals(verifyNumberRequest.getVerifyNumber())) throw new CustomException(ErrorCode.VERIFY_NUMBER_MISMATCH);
        else messageRepository.deleteMessage(message.getId());

    }

    @Override
    @Transactional
    public void deleteVerifyMessages(){
        List<Message> messages = messageRepository.findAll();
        List<Message> expiredMessages = messages.stream()
                .filter(message -> Duration.between(message.getUpdatedAt(), LocalDateTime.now()).toMinutes() > 3)
                .toList();

        if (!expiredMessages.isEmpty()) messageRepository.deleteAll(expiredMessages);
    }
}
