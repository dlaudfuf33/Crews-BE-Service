package org.crews.service;

import org.crews.config.AESConfig;
import org.crews.dto.core.AccountIssuedResponse;
import org.crews.dto.core.ProductRequest;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.AccountType;
import org.crews.model.constants.AgitRole;
import org.crews.repository.*;
import org.crews.utils.AESUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "CORE_API_CORE_URL=http://localhost:8081"
})
class AccountServiceTest {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceTest.class);

    @Autowired
    private AccountService accountService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private AgitRepository agitRepository;

    @MockBean
    private MembershipRepository membershipRepository;

    @MockBean
    private AgitAndAccountRepository agitAndAccountRepository;

    @MockBean
    private BankRepository bankRepository;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private CoreService coreService;

    // Mock된 AESUtil의 암호화/복호화 테스트 데이터
    private static final String ENCRYPTED_NAME = "EncryptedTestMember";
    private static final String ENCRYPTED_ACCOUNT = "EncryptedAccount123";

    @BeforeEach
    void setUp() {
        // AESConfig 초기화
        AESConfig aesConfig = new AESConfig();
        aesConfig.setSecretKey("crewsSecretKey12"); // AES 암호화를 위한 키
        aesConfig.setIv("abcdefghijklmnop");       // AES 초기화 벡터
        new AESUtil(aesConfig); // AESUtil 내부에서 aesConfig를 사용할 수 있도록 설정
    }

    @Test
    void accountIssued_성공() {
        // AESUtil의 정적 메서드를 Mock 처리
        try (MockedStatic<AESUtil> mockedAES = Mockito.mockStatic(AESUtil.class)) {
            // AESUtil 암호화/복호화 로직에 대해 예상 동작 설정
            mockedAES.when(() -> AESUtil.encrypt("테스트멤버")).thenReturn(ENCRYPTED_NAME);
            mockedAES.when(() -> AESUtil.encrypt("123456789")).thenReturn(ENCRYPTED_ACCOUNT);
            mockedAES.when(() -> AESUtil.decrypt(ENCRYPTED_NAME)).thenReturn("테스트멤버");
            mockedAES.when(() -> AESUtil.decrypt(ENCRYPTED_ACCOUNT)).thenReturn("123456789");

            // given: 테스트에 필요한 데이터를 생성하고 Mock 동작 정의
            Long agitId = 1L; // 모임 ID
            Long memberId = 1L; // 회원 ID
            ProductRequest productRequest = ProductRequest.builder().productId(1L).build(); // 상품 요청 데이터

            // 모임(Agit) 객체 Mock 설정
            Agit agit = new Agit();
            agit.setId(agitId);
            agit.setAgitName("테스트 모임");

            // 회원(Member) 객체 Mock 설정
            Member member = new Member();
            member.setId(memberId);
            member.setName(ENCRYPTED_NAME); // 암호화된 이름
            member.setCi("6rNw8sbO5ch1MPfs+bZ89d8eD3gthcBX7zq1M6I6sALL7GXcijAaJm56XGo3gzqJcCgkjQGrPW7L000ufofWZA==");

            // 회원과 모임의 관계(Membership) 객체 Mock 설정
            Membership membership = new Membership();
            membership.setMember(member);

            // 은행(Bank) 객체 Mock 설정
            Bank bank = new Bank();
            bank.setBankCode("777");
            bank.setBankName("테스트 은행");

            // 외부 CoreService 호출 결과 Mock 설정
            AccountIssuedResponse mockCoreResponse = AccountIssuedResponse.builder()
                    .accountNumber("123456789") // 생성된 계좌 번호
                    .balance(BigDecimal.valueOf(10000)) // 초기 잔액
                    .bankCode("777") // 은행 코드
                    .fintechUseNum("FT1234567890") // 핀테크 사용자 번호
                    .productName("테스트 상품") // 상품 이름
                    .accountType(AccountType.CREW) // 개인 계좌 타입
                    .build();

            // 저장된 Account 객체 Mock 설정
            Account savedAccount = Account.builder()
                    .accountNumber(ENCRYPTED_ACCOUNT) // 암호화된 계좌 번호
                    .bank(bank)
                    .balance(BigDecimal.valueOf(10000))
                    .member(member)
                    .productName("테스트 상품")
                    .accountType(AccountType.PERSONAL)
                    .fintecNumber("FT1234567890")
                    .build();

            // Mock 동작 정의
            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member)); // 회원 조회 Mock
            when(agitRepository.findById(agitId)).thenReturn(Optional.of(agit)); // 모임 조회 Mock
            when(membershipRepository.findByAgitAndAgitRole(agit, AgitRole.LEADER))
                    .thenReturn(Optional.of(membership)); // 회원과 모임의 관계 조회 Mock
            when(agitAndAccountRepository.findByAgit(agit)).thenReturn(Optional.empty()); // 모임과 계좌 연결 여부 확인 Mock
            when(coreService.accountIssued(any())).thenReturn(mockCoreResponse); // 외부 CoreService 호출 Mock
            when(bankRepository.findByBankCode("777")).thenReturn(Optional.of(bank)); // 은행 정보 조회 Mock
            when(accountRepository.save(any(Account.class))).thenReturn(savedAccount); // 계좌 저장 Mock

            // when: 테스트 대상 메서드 실행
            AccountIssuedResponse response = accountService.accountIssued(agitId, memberId, productRequest, AgitRole.LEADER);

            // then: 결과 검증
            Assertions.assertNotNull(response); // 응답이 null이 아님
            Assertions.assertEquals("123456789", response.getAccountNumber()); // 계좌 번호가 예상값과 일치
            Assertions.assertEquals(BigDecimal.valueOf(10000), response.getBalance()); // 잔액이 예상값과 일치

            log.info("계좌 발급 성공!");
            log.info("응답 계좌 번호: {}", response.getAccountNumber());
            log.info("응답 잔액: {}", response.getBalance());
            log.info("응답 은행 코드: {}", response.getBankCode());

            // Mock 객체의 메서드 호출 여부 검증
            verify(memberRepository, times(1)).findById(memberId);
            verify(agitRepository, times(1)).findById(agitId);
            verify(coreService, times(1)).accountIssued(any());
            verify(accountRepository, times(1)).save(any(Account.class));
        }
    }

    @Test
    void accountIssued_회원없음_예외발생() {
        try (MockedStatic<AESUtil> mockedAES = Mockito.mockStatic(AESUtil.class)) {
            // Mock AESUtil
            mockedAES.when(() -> AESUtil.encrypt("테스트멤버")).thenReturn(ENCRYPTED_NAME);

            // given
            Long agitId = 1L;
            Long memberId = 999L; // 존재하지 않는 회원 ID
            ProductRequest productRequest = ProductRequest.builder().productId(1L).build();

            // Mock 설정
            when(memberRepository.findById(memberId)).thenReturn(Optional.empty()); // 회원 없음

            // when & then
            CustomException exception = Assertions.assertThrows(CustomException.class,
                    () -> accountService.accountIssued(agitId, memberId, productRequest, AgitRole.LEADER));

            // 예외 메시지와 코드 검증
            Assertions.assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode(), "예외 코드가 일치하지 않습니다.");
            log.error("회원 ID {}에 해당하는 회원이 없습니다.", memberId);
            log.error("발생한 예외 메시지: {}", exception.getMessage());

            // Mock 검증
            verify(memberRepository, times(1)).findById(memberId);
            verifyNoInteractions(agitRepository, coreService, accountRepository);
        }
    }

}
