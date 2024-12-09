package org.crews.service;

import org.crews.dto.core.AttachResponse;
import org.crews.dto.core.TransferApiResponse;
import org.crews.dto.core.TransferResponse;
import org.crews.dto.request.AccountDeleteRequest;
import org.crews.dto.request.AttachAccountRequest;
import org.crews.dto.request.PaymentRequest;
import org.crews.dto.response.TransferMsgResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.AccountType;
import org.crews.model.constants.AgitRole;
import org.crews.repository.*;
import org.crews.utils.AESUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class MemberServiceImplTest {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceTest.class);

    @Autowired
    private MemberServiceImpl memberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private BankRepository bankRepository;

    @MockBean
    private AgitRepository agitRepository;

    @MockBean
    private MembershipRepository membershipRepository;

    @MockBean
    private DuesRepository duesRepository;

    @MockBean
    private CoreService coreService;

    @Test
    void attachAccount_성공() {
        try (MockedStatic<AESUtil> mockedAES = Mockito.mockStatic(AESUtil.class)) {
            // Mock AESUtil
            mockedAES.when(() -> AESUtil.encrypt("123456789")).thenReturn("EncryptedAccount123");
            mockedAES.when(() -> AESUtil.encrypt("987654321")).thenReturn("EncryptedAccount987");

            // given
            Long memberId = 1L;
            AttachAccountRequest attachAccountRequest = new AttachAccountRequest(List.of("123456789", "987654321"));

            Member member = new Member();
            member.setId(memberId);
            member.setCi("6rNw8sbO5ch1MPfs+bZ89d8eD3gthcBX7zq1M6I6sALL7GXcijAaJm56XGo3gzqJcCgkjQGrPW7L000ufofWZA==");

            AttachResponse attachResponse1 = AttachResponse.builder()
                    .accountNumber("123456789")
                    .fintechUseNum("F123456789")
                    .bankCode("777")
                    .productName("테스트상품1")
                    .balance(BigDecimal.valueOf(10000))
                    .accountType(AccountType.PERSONAL)
                    .build();

            AttachResponse attachResponse2 = AttachResponse.builder()
                    .accountNumber("987654321")
                    .fintechUseNum("F987654321")
                    .bankCode("888")
                    .productName("테스트상품2")
                    .balance(BigDecimal.valueOf(20000))
                    .accountType(AccountType.PERSONAL)
                    .build();

            Bank bank1 = new Bank();
            bank1.setBankCode("777");
            bank1.setBankName("은행1");

            Bank bank2 = new Bank();
            bank2.setBankCode("888");
            bank2.setBankName("은행2");

            // Mock 설정
            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(bankRepository.findByBankCode("777")).thenReturn(Optional.of(bank1));
            when(bankRepository.findByBankCode("888")).thenReturn(Optional.of(bank2));
            when(accountRepository.existsByMemberIdAndAccountNumber(memberId, "EncryptedAccount123")).thenReturn(false);
            when(accountRepository.existsByMemberIdAndAccountNumber(memberId, "EncryptedAccount987")).thenReturn(false);
            when(coreService.attachAccount(any())).thenReturn(List.of(attachResponse1, attachResponse2));

            // when
            memberService.attachAccount(memberId, attachAccountRequest);

            // then
            verify(accountRepository, times(2)).save(any(Account.class));
            log.info("회원 '{}'의 계좌가 성공적으로 등록되었습니다: [계좌 1: {}, 계좌 2: {}]",
                    memberId, attachResponse1.getAccountNumber(), attachResponse2.getAccountNumber());
        }
    }

    @Test
    void attachAccount_계좌중복_예외발생() {
        try (MockedStatic<AESUtil> mockedAES = Mockito.mockStatic(AESUtil.class)) {
            // Mock AESUtil
            mockedAES.when(() -> AESUtil.encrypt("123456789")).thenReturn("EncryptedAccount123");

            // given
            Long memberId = 1L;
            AttachAccountRequest attachAccountRequest = new AttachAccountRequest(List.of("123456789"));

            Member member = new Member();
            member.setId(memberId);
            member.setCi("6rNw8sbO5ch1MPfs+bZ89d8eD3gthcBX7zq1M6I6sALL7GXcijAaJm56XGo3gzqJcCgkjQGrPW7L000ufofWZA==");

            AttachResponse attachResponse = AttachResponse.builder()
                    .accountNumber("123456789")
                    .fintechUseNum("F123456789")
                    .bankCode("007")
                    .productName("테스트상품1")
                    .balance(BigDecimal.valueOf(10000))
                    .accountType(AccountType.PERSONAL)
                    .build();

            Bank bank = new Bank();
            bank.setBankCode("007");
            bank.setBankName("은행1");

            // Mock 설정
            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(bankRepository.findByBankCode("007")).thenReturn(Optional.of(bank));
            when(accountRepository.existsByMemberIdAndAccountNumber(memberId, "EncryptedAccount123")).thenReturn(true);
            when(coreService.attachAccount(any())).thenReturn(List.of(attachResponse));

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> memberService.attachAccount(memberId, attachAccountRequest));

            assertEquals(ErrorCode.ACCOUNT_ALREADY_EXISTS, exception.getErrorCode());
            log.warn("회원 '{}'의 계좌 등록 중 중복 계좌가 발견되었습니다: 계좌 '{}'", memberId, attachResponse.getAccountNumber());
        }
    }

    @Test
    void deleteMyAccounts_성공() {
        // given
        Long memberId = 1L;
        Long accountId = 101L;

        AccountDeleteRequest accountDeleteRequest = new AccountDeleteRequest(accountId);

        Account account = new Account();
        account.setId(accountId);

        // Mock 설정
        when(accountRepository.findByIdAndMemberId(accountId, memberId)).thenReturn(Optional.of(account));

        // when
        memberService.deleteMyAccounts(memberId, accountDeleteRequest);

        // then
        verify(accountRepository, times(1)).delete(account);
        log.info("회원 '{}'의 계좌 '{}'가 성공적으로 삭제되었습니다.", memberId, accountId);
    }

    @Test
    void deleteMyAccounts_계좌없음_예외발생() {
        // given
        Long memberId = 1L;
        Long accountId = 101L;

        AccountDeleteRequest accountDeleteRequest = new AccountDeleteRequest(accountId);

        // Mock 설정: 계좌 찾기 실패
        when(accountRepository.findByIdAndMemberId(accountId, memberId)).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> memberService.deleteMyAccounts(memberId, accountDeleteRequest));

        assertEquals(ErrorCode.ACCOUNT_ID_NOT_FOUND, exception.getErrorCode());
        log.warn("회원 '{}'의 계좌 '{}'를 찾을 수 없습니다.", memberId, accountId);
    }


    @Test
    void paymentFee_성공() {
        try (MockedStatic<AESUtil> mockedAES = Mockito.mockStatic(AESUtil.class)) {
            // AESUtil Mock 설정
            mockedAES.when(() -> AESUtil.decrypt("EncryptedAccount987")).thenReturn("987654321");

            // given
            Long memberId = 1L;
            Long agitId = 2L;
            Long myAccountId = 3L;
            Long crewAccountId = 4L;

            PaymentRequest paymentRequest = PaymentRequest.builder()
                    .pinNumber("000000") // 사용자가 입력하는 PIN 번호
                    .amount(BigDecimal.valueOf(10000))
                    .myAccountId(myAccountId)
                    .crewAccountId(crewAccountId)
                    .agitId(agitId)
                    .build();

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            Member member = new Member();
            member.setId(memberId);
            member.setPinNumber(passwordEncoder.encode("000000")); // PIN 번호를 암호화하여 Mock 설정

            Account myAccount = new Account();
            myAccount.setId(myAccountId);
            myAccount.setFintecNumber("F987654321");
            myAccount.setProductName("테스트 상품");

            Account crewAccount = new Account();
            crewAccount.setId(crewAccountId);
            crewAccount.setAccountNumber("EncryptedAccount987");

            Agit agit = new Agit();
            agit.setId(agitId);
            agit.setAgitName("테스트 모임");
            CommonDues commonDues = new CommonDues();
            agit.setCommonDues(commonDues);

            Membership membership = new Membership();
            membership.setId(5L);

            TransferResponse transferResponse = TransferResponse.builder()
                    .amount(BigDecimal.valueOf(10000))
                    .build();

            // Mock 설정
            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(accountRepository.findByIdAndMemberId(myAccountId, memberId)).thenReturn(Optional.of(myAccount));
            when(accountRepository.findById(crewAccountId)).thenReturn(Optional.of(crewAccount));
            when(agitRepository.findByIdWithCommonDues(agitId)).thenReturn(Optional.of(agit));
            when(membershipRepository.findByMemberIdAndAgitId(memberId, agit.getId())).thenReturn(Optional.of(membership));
            when(coreService.transferFee(any())).thenReturn(TransferApiResponse.builder().data(transferResponse).build());

            // when
            TransferMsgResponse response = memberService.paymentFee(memberId, paymentRequest);

            // then
            assertEquals("10000원 이체 성공하였습니다!", response.getMessage());
            verify(duesRepository, times(1)).save(any(Dues.class));

            log.info("테스트: 회원 '{}'이 모임 '{}'에 {}원을 납부했습니다.", memberId, agit.getAgitName(), paymentRequest.getAmount());

        }
    }


    @Test
    void paymentFee_핀번호불일치_예외발생() {
        // given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Long memberId = 1L;
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .pinNumber("777777") // 잘못된 PIN 번호
                .amount(BigDecimal.valueOf(10000))
                .myAccountId(3L)
                .crewAccountId(4L)
                .agitId(2L)
                .build();

        Member member = new Member();
        member.setId(memberId);
        member.setPinNumber(passwordEncoder.encode("000000")); // PIN 번호를 암호화하여 Mock 설정

        // Mock 설정
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // when & then
        CustomException exception = assertThrows(CustomException.class,
                () -> memberService.paymentFee(memberId, paymentRequest));

        assertEquals(ErrorCode.INVALID_OLD_PASSWORD, exception.getErrorCode());
        log.warn("테스트: 회원 '{}'이 잘못된 PIN 번호 '{}'로 인증을 시도했으나 실패하였습니다.", memberId, paymentRequest.getPinNumber());
    }

    @Test
    void paymentFee_잔액부족_예외발생() {
        // given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Long memberId = 1L;
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .pinNumber("000000")
                .amount(BigDecimal.valueOf(10000))
                .myAccountId(3L)
                .crewAccountId(4L)
                .agitId(2L)
                .build();

        Member member = new Member();
        member.setId(memberId);
        member.setPinNumber(passwordEncoder.encode("000000"));

        Account myAccount = new Account();
        myAccount.setId(3L);
        myAccount.setFintecNumber("F987654321");
        myAccount.setBalance(BigDecimal.valueOf(5000));


        Account crewAccount = new Account();
        crewAccount.setId(4L);
        crewAccount.setAccountNumber("EncryptedAccount987");

        Agit agit = new Agit();
        agit.setId(2L);
        agit.setAgitName("테스트 모임");

        Membership membership = new Membership();
        membership.setId(1L);
        membership.setMember(member);
        membership.setAgit(agit);
        membership.setAgitRole(AgitRole.MEMBER);

        // Mock 설정
        try (MockedStatic<AESUtil> mockedAES = Mockito.mockStatic(AESUtil.class)) {
            mockedAES.when(() -> AESUtil.decrypt("EncryptedAccount987")).thenReturn("987654321");

            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(accountRepository.findByIdAndMemberId(3L, memberId)).thenReturn(Optional.of(myAccount));
            when(accountRepository.findById(4L)).thenReturn(Optional.of(crewAccount));
            when(agitRepository.findByIdWithCommonDues(2L)).thenReturn(Optional.of(agit));
            when(membershipRepository.findByMemberIdAndAgitId(memberId, agit.getId())).thenReturn(Optional.of(membership));
            when(coreService.transferFee(any()))
                    .thenThrow(new WebServerException(
                            "{\"error\":{\"message\":\"잔액이 부족합니다.\"}}",
                            new RuntimeException("잔액 부족 예외")
                    ));

            // when & then
            CustomException exception = assertThrows(CustomException.class,
                    () -> memberService.paymentFee(memberId, paymentRequest));

            // 로깅 추가
            log.warn("테스트: 회원 '{}'이 아지트 '{}'에 '{}'원을 납부하려 했으나 잔액이 {}으로 잔액 부족 예외 발생.", memberId, agit.getAgitName(), paymentRequest.getAmount(),myAccount.getBalance());

            assertEquals(ErrorCode.INSUFFICIENT_BALANCE, exception.getErrorCode());
        }
    }

}

