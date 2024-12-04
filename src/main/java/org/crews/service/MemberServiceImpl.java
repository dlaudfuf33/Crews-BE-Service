package org.crews.service;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.*;
import org.crews.dto.request.*;
import org.crews.dto.response.*;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.jwt.JWTUtil;
import org.crews.model.*;
import org.crews.model.constants.AccountType;
import org.crews.repository.*;
import org.crews.utils.AESUtil;
import org.crews.utils.CIGenerator;
import org.crews.utils.MaskedNumber;
import org.crews.utils.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

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
    private final MemberShipRepository memberShipRepository;
    private final CardRepository cardRepository;
    private final AuthUtil authUtil;
    private final MessageRepository messageRepository;
    private final DuesRepository duesRepository;
    private final AgitRepository agitRepository;


    @Override
    @Transactional
    public MemberResponse signUp(MemberRequest memberRequest) {
        try {
            // 이메일 중복 확인
            boolean isExist = memberRepository.existsByEmail(aesUtil.encrypt(memberRequest.getEmail()));
            log.info(String.valueOf(isExist));
            if (isExist) {
                log.warn("이미 존재하는 이메일으로 회원가입 시도: {}", memberRequest.getEmail());
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
            member.setNickName(NicknameUtills.generateRandomNickname());
            log.debug("랜덤 닉네임 설정: {}", member.getNickName());

            // 주소 처리
            Address address = addressService.findOrCreateAddress(memberRequest.getAddressDo(), memberRequest.getAddressSi(), memberRequest.getAddressGuGun(), memberRequest.getAddressDong());
            member.setAddress(address);
            // 핀 번호 설정
            member.setPinNumber(bCryptPasswordEncoder.encode(memberRequest.getPinNumber()));
            // 회원 저장
            Member savedMember = memberRepository.save(member);
            // 회원 관심사 설정 (기본 값)
            memberAndInterestingRepository.save(MemberAndInteresting.builder().member(savedMember).interesting(interestingRepository.findById(1L).orElseThrow()).build());


            CIRequest ciRequest = CIRequest.builder().name(memberRequest.getName()).email(memberRequest.getEmail()).phone(memberRequest.getPhoneNumber()).ci(ci).build();
            Mono<AccountIssuedResponse> stringMono = coreService.sendCICode(ciRequest);
            AccountIssuedResponse response = stringMono.block();
            if (response == null)
                throw new CustomException(ErrorCode.CI_CODE_SEND_ERROR);
            Bank bank = bankRepository.findByBankCode(response.getBankCode()).orElseThrow(
                    () -> new CustomException(ErrorCode.WRONG_BANKCODE)
            );
            Account account = Account.builder().bank(bank).member(member).maskedAccountNumber(MaskedNumber.maskedAccountNumber(response.getAccountNumber()))
                    .accountNumber(AESUtil.encrypt(response.getAccountNumber())).balance(response.getBalance()).
                    accountType(response.getAccountType()).fintecNumber(response.getFintechUseNum()).productName(response.getProductName()).build();
            accountRepository.save(account);
            log.info("회원가입 성공: 회원ID={}, 이메일={}", savedMember.getId(), memberRequest.getEmail());
            return MemberResponse.from(savedMember);

        } catch (CustomException ce) {
            log.error("회원가입 중 CustomException 발생: {}", ce.getErrorCode(), ce);
            throw ce;
        } catch (Exception e) {
            log.error("회원가입 중 예상치 못한 예외 발생: ", e);
            throw new CustomException(ErrorCode.DATABASE_ACCESS_FAILED, e);
        }
    }

    @Override
    public ResponseEntity refreshCheck(String refresh) {
        String tokenName = "refresh";
        if (refresh == null) {

            //response status code
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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

        String newAccessToken = jwtUtil.createJwt(accessTokenName, username, role, memberId, 864000000L);
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
        return MyProfileResponse.from(memberRepository.findByIdWithInterestings(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)));

    }

    @Override
    public MyinfoResponse getMyinfo(Long memberId) {
        return MyinfoResponse.from(memberRepository.findByIdWithAddresses(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)));
    }

    @Override
    public List<InterestResponse> getMyInterests(Long memberId) {
        return memberRepository.findByIdWithInterestings(memberId).orElseThrow(() -> new CustomException(ErrorCode.INTERESTS_NOT_FOUND)).getMemberAndInterestings().stream().map(MemberAndInteresting::getInteresting).map(InterestResponse::from).toList();
    }

    @Override
    public boolean validateEmail(EmailRequest request) {
        return memberRepository.existsByEmail(aesUtil.encrypt(request.getEmail()));
    }

    @Override
    public MyNicknameResponse getMyNickname(Long memberId) {
        return MyNicknameResponse.from(memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)));
    }

    @Override
    @Transactional
    public MyNicknameResponse updateMyNickname(Long memberId, MyNicknameRequest myNicknameRequest) {
        if (NicknameUtills.validationNickname(myNicknameRequest.getNickname())) {
            throw new CustomException(ErrorCode.INVALID_NICKNAME);
        }
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.setNickName(myNicknameRequest.getNickname());
        Member savedMember = memberRepository.saveAndFlush(member);
        log.info("회원:'{}'이 닉네임을 '{}'으로 변경하였습니다.", member.getId(), member.getNickName());
        return MyNicknameResponse.from(savedMember);
    }

    @Override
    @Transactional
    public void updateMyInterestings(Long memberId, InterestsUpdateRequest interestsUpdateRequest) {
        StringBuilder interestsBuilder = new StringBuilder();
        Member foundMember = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        memberAndInterestingRepository.deleteByMemberIdCustom(memberId);

        interestsUpdateRequest.getInterests().forEach(item -> {
            MemberAndInteresting memberAndInteresting = new MemberAndInteresting();
            memberAndInteresting.setMember(foundMember);
            memberAndInteresting.setInteresting(interestingRepository.findById(item.getInterestId()).orElseThrow(() -> new CustomException(ErrorCode.INTERESTS_NOT_FOUND)));
            memberAndInterestingRepository.save(memberAndInteresting);
            if (!interestsBuilder.isEmpty()) {
                interestsBuilder.append(", ");
            }
            interestsBuilder.append(memberAndInteresting.getInteresting().getName());
        });
        log.info("회원:'{}'이 관심사를 '{}'으로 변경하였습니다.", foundMember.getId(), interestsBuilder.toString());
    }

    @Override
    public AddressResponse getMyAddresses(Long memberId) {
        return AddressResponse.from(memberRepository.findByWithAddress(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)).getAddress());
    }

    @Override
    @Transactional
    public void updateMyAddresses(Long memberId, AddressRequest addressRequest) {
        Member member = memberRepository.findByWithAddress(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        String oldAddress = member.getAddress() != null ? member.getAddress().getUniqueAddressKey() : "없음";

        member.setAddress(addressService.findOrCreateAddress(addressRequest.getDoName(), addressRequest.getSiName(), addressRequest.getGuName(), addressRequest.getDongName()));
        memberRepository.save(member);
        log.info("회원 '{}'의 주소가 '{}'에서 '{}'으로 변경되었습니다.", member.getId(), oldAddress, member.getAddress().getUniqueAddressKey());
    }

    @Override
    @Transactional
    public void updatePassword(Long memberId, PasswordUpdateRequest passwordUpdateRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        validateOldPassword(member.getPassword(), passwordUpdateRequest.getOldPassword());

        validateNewPasswords(passwordUpdateRequest.getNewPassword(), passwordUpdateRequest.getConfirmPassword());

        String encryptedNewPassword = bCryptPasswordEncoder.encode(passwordUpdateRequest.getNewPassword());
        member.setPassword(encryptedNewPassword);
        memberRepository.save(member);
        log.info("회원 '{}'가 비밀번호를 변경하었습니다.", memberId);
    }

    @Override
    public List<AgitResponse> getMyAgits(Long memberId) {
        List<Agit> as = memberShipRepository.findMembershipsWithAgitDetailsByMemberId(memberId).stream().map(Membership::getAgit).toList();
        return as.stream().map(AgitResponse::from).toList();
    }

    @Override
    public List<AgitCardsResponse> getMyAgitsCards(Long memberId) {
        return cardRepository.findAgitCardsByMemberId(memberId);
    }

    @Override
    @Transactional
    public void deleteMyAgitsCards(Long memberId, CardDeleteRequest cardDeleteRequest) {
        Long cardId = cardDeleteRequest.getCardId();
        Card foundCard = cardRepository.findByIdAndMemberId(cardId, memberId).orElseThrow(() -> new CustomException(ErrorCode.CARD_NOT_MATCHED_MEMBER));
        maskingCard(foundCard);
        log.info("회원 '{}'가 모임카드'{}'를 해지하였습니다.", memberId, foundCard);
    }

    @Override
    @Transactional
    public List<AccountsResponse> getMyAccounts(Long memberId) {
        List<Account> personalAccounts = accountRepository.findAccountsByMemberId(memberId);

        if (!personalAccounts.isEmpty()) {
            BalanceRequest balanceRequest = BalanceRequest.from(personalAccounts);

            try {
                List<FintechBalancePairResponse> fintechBalancePairResponseList = coreService.balanceLoad(balanceRequest);

                Map<String, BigDecimal> balanceMap = fintechBalancePairResponseList.stream().collect(Collectors.toMap(FintechBalancePairResponse::getFintechNumber, FintechBalancePairResponse::getBalance, (existing, replacement) -> replacement));

                personalAccounts.forEach(account -> {
                    BigDecimal updatedBalance = balanceMap.get(account.getFintecNumber());
                    if (updatedBalance != null) {
                        account.setBalance(updatedBalance);
                    }
                });
            } catch (Exception e) {
                log.error("코어 서비스에서 잔액을 불러오는 중 오류 발생. memberId {}: {}", memberId, e.getMessage());
            }
        }
        log.info("회원 '{}'가 등록된 본인계좌 조회 하였습니다.", memberId);
        return personalAccounts.stream().map(AccountsResponse::from).toList();
    }

    @Override
    @Transactional
    public void deleteMyAccounts(Long memberId, AccountDeleteRequest accountDeleteRequest) {
        Account account = accountRepository.findByIdAndMemberId(accountDeleteRequest.getAccountId(), memberId).orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_ID_NOT_FOUND));

        if (!account.getCards().isEmpty()) {
            account.getCards().forEach(Card::maskCard);
        }

        accountRepository.delete(account);
        log.info("회원 '{}'가 등록된 본인계좌 '{}'를 삭제하였습니다.", memberId, account.getId());
    }

    @Override
    @Transactional
    public void attachAccount(Long memberId, AttachAccountRequest attachAccountRequest) {
        try {
            Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

            FintechNumRequest fintechNumRequest = new FintechNumRequest(member.getCi(), attachAccountRequest.getAccountNumbers());
            List<AttachResponse> attachResponses = coreService.attachAccount(fintechNumRequest);
            if (attachResponses.isEmpty()) {
                throw new CustomException(ErrorCode.NO_ACCOUNTS_RETURNED);
            }

            for (AttachResponse attachResponse : attachResponses) {
                Bank bank = bankRepository.findByBankCode(attachResponse.getBankCode()).orElseThrow(() -> new CustomException(ErrorCode.BANK_NOT_FOUND));

                boolean accountExists = accountRepository.existsByMemberIdAndAccountNumber(memberId, AESUtil.encrypt(attachResponse.getAccountNumber()));
                if (accountExists) {
                    throw new CustomException(ErrorCode.ACCOUNT_ALREADY_EXISTS);
                }

                Account account = Account.builder().accountNumber(AESUtil.encrypt(attachResponse.getAccountNumber())).maskedAccountNumber(maskedAccountNumber(attachResponse.getAccountNumber())).accountType(attachResponse.getAccountType()).productName(attachResponse.getProductName()).bank(bank).balance(attachResponse.getBalance()).fintecNumber(attachResponse.getFintechUseNum()).member(member).build();
                log.info("확인 {}.", account.getFintecNumber());

                accountRepository.save(account);
            }

            log.info("회원 '{}'의 계좌가 성공적으로 등록되었습니다.", memberId);

        } catch (CustomException e) {
            log.error("계좌 등록 중 오류 발생: {}", e.getErrorCode().getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("계좌 등록 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.UNKNOWN_EXCEPTION, e);
        }
    }


    @Override
    public List<AccountResponse> getAccountInfoFromCore(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        try {

            List<AccountResponse> coreAccounts = coreService.findCoreSideAccounts(MemberToCoreRequest.from(member));

            if (coreAccounts == null) {
                log.warn("Core 서비스에서 계좌 정보를 가져오지 못했습니다. memberId: {}", memberId);
                return List.of();
            }

            List<Account> existingAccounts = accountRepository.findAccountsByMemberId(memberId);
            Set<String> existingAccountNumbers = existingAccounts.stream().map(Account::getAccountNumber).collect(Collectors.toSet());

            List<AccountResponse> newAccounts = coreAccounts.stream().filter(accountResponse -> accountResponse.getAccountType() == AccountType.PERSONAL).filter(accountResponse -> {
                boolean isNew = !existingAccountNumbers.contains(AESUtil.encrypt(accountResponse.getAccountNumber()));
                if (!isNew) {
                    log.info("이미 등록된 계좌 제외: '{}'", accountResponse.getAccountNumber());
                }
                return isNew;
            }).toList();

            log.info("회원 '{}'에 대한 PERSONAL 타입 계좌 중 신규 계좌의 수: '{}'", memberId, newAccounts.size());
            return newAccounts;
        } catch (Exception e) {
            log.warn("Core 서비스에서 계좌정보 조회를 실패했습니다. {}", e.getMessage());
            return List.of();
        }
    }

    public AgitAccountInfoListResponse getAgitsAccountsInfo(Long memberId) {
        List<Membership> memberships = memberShipRepository.findMembershipsWithDetailsByMemberId(memberId);
        if (memberships.isEmpty()) {
            throw new CustomException(ErrorCode.NO_ASSOCIATED_GROUP);
        }

        List<AgitAccountInfoResponse> agitAccountInfoResponses = memberships.stream().filter(membership -> {
            boolean valid = membership.getAgit() != null && membership.getAgit().getAgitAndAccount() != null && membership.getAgit().getAgitAndAccount().getAccount() != null;

            if (!valid) {
                logInvalidMembership(membership);
            }
            return valid;
        }).map(this::mapToAgitAccountInfoResponse).toList();

        AgitAccountInfoListResponse response = new AgitAccountInfoListResponse();
        response.setCrewAccounts(agitAccountInfoResponses);

        return response;
    }

    @Override
    public List<WithdrawResponse> getwithdraws(Long memberId, Long myAccountId, Long crewAccountId) {
        try {
            Account myAccount = accountRepository.findByIdAndMemberId(myAccountId, memberId)
                    .orElseThrow(() -> {
                        log.error("내 계좌를 찾을 수 없음: 회원ID={}, 계좌ID={}", memberId, myAccountId);
                        return new CustomException(ErrorCode.NO_ACCOUNTS_RETURNED);
                    });

            Account agitAccount = accountRepository.findById(crewAccountId)
                    .orElseThrow(() -> {
                        log.error("상대 계좌를 찾을 수 없음: 계좌ID={}", crewAccountId);
                        return new CustomException(ErrorCode.AGIT_ACCOUNT_NOT_FOUND);
                    });

            String decryptedAgitAccountNumber = AESUtil.decrypt(agitAccount.getAccountNumber());
            log.debug("상대 계좌 번호 복호화 완료: {}", decryptedAgitAccountNumber);

            TransactionDetailResponse transactionDetailResponse = coreService.getWithdrawHistory(WithdrawTransactionRequest.from(myAccount));
            log.info("출금 내역 조회 응답: 회원ID={}, 거래내역수={}", memberId, transactionDetailResponse.getTranList().size());

            List<WithdrawResponse> withdrawResponses = transactionDetailResponse.getTranList().stream()
                    .filter(tx -> decryptedAgitAccountNumber.equals(tx.getCounterpartyAccountNum()))
                    .map(tx -> new WithdrawResponse(tx.getTransactionTime(), agitAccount.getAgitAndAccount().getAgit().getAgitName(), tx.getCounterpartyBankCode(), tx.getCounterpartyAccountNum(), tx.getTranAmount()))
                    .toList();

            log.info("출금 내역 조회 완료: 회원ID={}, 결과수={}", memberId, withdrawResponses.size());
            return withdrawResponses;
        } catch (CustomException ce) {
            log.error("출금 내역 조회 중 오류 발생: 회원ID={}, 오류코드={}", memberId, ce.getErrorCode(), ce);
            throw ce;
        } catch (Exception e) {
            log.error("출금 내역 조회 중 예상치 못한 예외 발생: 회원ID={}, 오류 메시={}", memberId, e.getMessage(), e);
            throw new CustomException(ErrorCode.DATABASE_ACCESS_FAILED, e);
        }
    }

    @Override
    @Transactional
    public TransferMsgResponse paymentFee(Long memberId, PaymentRequest paymentRequest) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        validateOldPassword(member.getPinNumber(), paymentRequest.getPinNumber());
        Account myAccount = accountRepository.findByIdAndMemberId(paymentRequest.getMyAccountId(), memberId).orElseThrow(
                () -> new CustomException(ErrorCode.NO_ACCOUNTS_RETURNED));
        Account agitAccount = accountRepository.findById(paymentRequest.getCrewAccountId()).orElseThrow(
                () -> new CustomException(ErrorCode.NO_ACCOUNTS_RETURNED));

        Agit agit = agitRepository.findByIdWithCommonDues(paymentRequest.getAgitId()).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));
        Membership membership = memberShipRepository.findByMemberIdAndAgitId(memberId, agit.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));
        log.warn("{}", paymentRequest.getAmount());
        TransferResponse transferResponse =
                coreService.transferFee(
                        TransferRequest.builder().
                                finUseNum(myAccount.getFintecNumber())
                                .recvAccountNum(AESUtil.decrypt(agitAccount.getAccountNumber()))
                                .amt(paymentRequest.getAmount())
                                .build()).getData();

        duesRepository.save(Dues.builder()
                .dueAmount(transferResponse.getAmount())
                .productName(myAccount.getProductName())
                .accountNumber(myAccount.getAccountNumber())
                .agitName(agit.getAgitName())
                .membership(membership)
                .isPayed(true)
                .dueDate(LocalDateTime.now())
                .commonDues(agit.getCommonDues())
                .build());

        return TransferMsgResponse.builder().message(transferResponse.getAmount() + "원 이체 성공하였습니다!").build();
    }

    @Override
    @Transactional
    public void updateMyProfile(Long memberId, ProfileImageRequest profileImageRequest) {
        memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        ).setProfileImage(profileImageRequest.getProfileImagePath());
    }

    @Override
    @Transactional
    public void deletetMyProfile(Long memberId) {
        memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        ).setProfileImage("");
    }

    @Override
    @Transactional
    public void leavCrews(Long memberId, LeavRequest leavRequest) {
        Member foundMember =
                memberRepository.findById(memberId).orElseThrow(
                        () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
                );
        validateOldPassword(foundMember.getPassword(), leavRequest.getPassword());
        foundMember.setDeleted(true);
    }


    @Override
    public FindMemberIdResponse findMemberId(FindMemberRequest findMemberRequest) {

        Member member = memberRepository.findByNameAndPhoneNumber(AESUtil.encrypt(findMemberRequest.getName()), AESUtil.encrypt(findMemberRequest.getPhoneNumber())).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return FindMemberIdResponse.from(member);
    }

    @Override
    @Transactional
    public void findMemberPw(FindMemberPwRequest findMemberPwRequest) {
        Member member = memberRepository.findByEmailAndNameAndPhoneNumber(AESUtil.encrypt(findMemberPwRequest.getEmail()), AESUtil.encrypt(findMemberPwRequest.getName()), AESUtil.encrypt(findMemberPwRequest.getPhoneNumber())).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        String temporary = authUtil.generateRandomPassword(10);
        member.setPassword(bCryptPasswordEncoder.encode(temporary));

        MessageUtil.send(AESUtil.decrypt(member.getPhoneNumber()), temporary);
    }

    @Override
    @Transactional
    public void getVerifyNumber(VerifyPhoneRequest verifyPhoneRequest) {
        String verifyNumber = authUtil.verifyRandomNumber();

        Message message = messageRepository.findByPhoneNumber(verifyPhoneRequest.getPhoneNumber()).orElse(new Message());
        message.setPhoneNumber(verifyPhoneRequest.getPhoneNumber());
        message.setVerifyNumber(verifyNumber);

        messageRepository.save(message);

        MessageUtil.send(verifyPhoneRequest.getPhoneNumber(), verifyNumber);
    }

    @Override
    @Transactional
    public void verifyNumberCheck(VerifyNumberRequest verifyNumberRequest) {
        Message message = messageRepository.findByPhoneNumber(verifyNumberRequest.getPhoneNumber()).orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));

        if (Duration.between(message.getUpdatedAt(), LocalDateTime.now()).toMinutes() > 3)
            throw new CustomException(ErrorCode.VERIFY_NUMBER_EXPIRED);

        if (!message.getVerifyNumber().equals(verifyNumberRequest.getVerifyNumber()))
            throw new CustomException(ErrorCode.VERIFY_NUMBER_MISMATCH);
        else messageRepository.deleteMessage(message.getId());
    }


    @Override
    @Transactional
    public void deleteVerifyMessages() {
        List<Message> messages = messageRepository.findAll();
        List<Message> expiredMessages = messages.stream().filter(message -> Duration.between(message.getUpdatedAt(), LocalDateTime.now()).toMinutes() > 3).toList();

        if (!expiredMessages.isEmpty()) messageRepository.deleteAll(expiredMessages);
    }


    private void validateOldPassword(String currentPassword, String oldPassword) {
        if (!bCryptPasswordEncoder.matches(oldPassword, currentPassword)) {
            throw new CustomException(ErrorCode.INVALID_OLD_PASSWORD);
        }
    }

    private void validateNewPasswords(String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
        }
    }

    private void maskingCard(Card card) {
        card.maskCard();
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

    private void logInvalidMembership(Membership membership) {
        if (membership.getAgit() == null) {
            log.info("Membership ID {}에 연결된 Agit이 없습니다.", membership.getId());
        } else if (membership.getAgit().getAgitAndAccount() == null) {
            log.info("Agit ID {}에 연결된 AgitAndAccount가 없습니다.", membership.getAgit().getId());
        } else if (membership.getAgit().getAgitAndAccount().getAccount() == null) {
            log.info("AgitAndAccount ID {}에 연결된 Account가 없습니다.", membership.getAgit().getAgitAndAccount().getId());
        }
    }

    private AgitAccountInfoResponse mapToAgitAccountInfoResponse(Membership membership) {
        Agit agit = membership.getAgit();
        AgitAndAccount agitAndAccount = agit.getAgitAndAccount();
        Account account = agitAndAccount.getAccount();
        Bank bank = account.getBank();
        CommonDues commonDues = agit.getCommonDues();

        BigDecimal amount = commonDues != null ? commonDues.getDueAmount() : BigDecimal.ZERO;

        // 특정 기간 동안의 납부 내역만 고려하여 paidAmount 계산
        BigDecimal paidAmount = calculatePaidAmount(membership, commonDues);

        // remainingAmount 계산
        BigDecimal remainingAmount = amount.subtract(paidAmount);
        if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
            remainingAmount = BigDecimal.ZERO;
        }

        // remainingAmount가 0이면 납부 완료로 표시
        boolean isPaid = remainingAmount.compareTo(BigDecimal.ZERO) == 0;

        return new AgitAccountInfoResponse(
                agit.getId(),
                agit.getAgitName(),
                account.getId(),
                bank != null ? bank.getBankImage() : "",
                account.getProductName(),
                AESUtil.decrypt(account.getAccountNumber()),
                commonDues != null ? commonDues.getDueDay() : 0,
                amount,
                remainingAmount,
                isPaid
        );
    }

    private BigDecimal calculatePaidAmount(Membership membership, CommonDues commonDues) {
        if (commonDues == null) {
            return BigDecimal.ZERO;
        }

        int dueDay = commonDues.getDueDay();
        LocalDateTime startDate;
        LocalDateTime endDate;

        LocalDateTime now = LocalDateTime.now();
        if (now.getDayOfMonth() <= dueDay) {
            // 현재 날짜가 1일부터 dueDay일 사이인 경우: 이전 달의 dueDay부터 이번 달의 dueDay까지
            YearMonth previousMonth = YearMonth.now().minusMonths(1);
            startDate = calculateStartDate(previousMonth, dueDay);
            endDate = calculateEndDate(YearMonth.now(), dueDay);
        } else {
            // 현재 날짜가 dueDay 이후인 경우: 이번 달의 dueDay부터 다음 달의 dueDay까지
            YearMonth currentMonth = YearMonth.now();
            startDate = calculateStartDate(currentMonth, dueDay);
            endDate = calculateEndDate(currentMonth.plusMonths(1), dueDay);
        }

        return duesRepository.findByMembershipAndCommonDues(membership, commonDues)
                .stream()
                .filter(dues -> {
                    LocalDateTime dueDate = dues.getDueDate();
                    return !dueDate.isBefore(startDate) && dueDate.isBefore(endDate);
                })
                .map(Dues::getDueAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private LocalDateTime calculateStartDate(YearMonth month, int dueDay) {
        // 주어진 월의 dueDay 일자 (예: 14일) 또는 해당 월의 마지막 일자를 반환합니다.
        int dayOfMonth = Math.min(dueDay, month.lengthOfMonth());
        return month.atDay(dayOfMonth).atStartOfDay();
    }

    private LocalDateTime calculateEndDate(YearMonth month, int dueDay) {
        // 주어진 월의 dueDay 다음날 자정 (예: 14일 + 1)을 반환합니다.
        int dayOfMonth = Math.min(dueDay, month.lengthOfMonth());
        return month.atDay(dayOfMonth).plusDays(1).atStartOfDay();
    }


    @Override
    public void verifyPinNumber(Long memberId, PinNumberRequest pinNumberRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!bCryptPasswordEncoder.matches(pinNumberRequest.getPinNumber(), member.getPinNumber())) {
            throw new CustomException(ErrorCode.VERIFY_PIN_MISMATCH);
        }
    }

    @Override
    @Transactional
    public void updatePinNumber(Long memberId, PinNumberRequest pinNumberRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        member.setPinNumber(bCryptPasswordEncoder.encode(pinNumberRequest.getPinNumber()));
    }
}

