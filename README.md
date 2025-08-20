# CREWS - 소모임 관리 및 금융 서비스 플랫폼


<img width="800" height="600" alt="이명렬-포트폴리오 2 0" src="https://github.com/user-attachments/assets/888ce682-4e57-40ec-93c4-341919932b97" />

CREWS는 코어 뱅킹 기반의 금융 서비스를 포함한 소모임 관리 플랫폼입니다.   
사용자는 모임 일정, 자산, 회비 등을 통합 관리하며, 금융 서비스를 활용한 다양한 기능을 제공받을 수 있습니다.

기존 소모임 운영은 회비 납부, 추적, 독촉, 회비 소비 내역 확인 등 여러 과정이 번거롭고 복잡하며 소모임 플랫폼과 은행 앱 등 다양한 서비스를 번갈아 사용하는 불편함이 있었습니다.   
CREWS는 이러한 문제를 해결하기 위해 소모임 운영과 회비 관리의 통합을 목표로 한 플랫폼에서 모든 과정을 쉽고 간편하게 처리할 수 있도록 설계 및 개발되었습니다.

<br>

팀 프로젝트 (5인)
- **진행 기간**: 2024.10 ~ 2024.12
- **역할 및 기여**: 백엔드 팀원
  - 금융 서버 구축 및 API 설계/개발
  - 서비스 서버 API 설계/개발
  - 데이터베이스 설계 및 구축     
  - 서버 간 통신(WebClient) 및 인증 구조 설계 ( API Key)
  - 비관적 락, 동시성 제어 등 데이터 정합성 보장 구조 설계 및 테스트
  - 클라우드 인프라 구성 (RDS, S3)

<br>

 ## 📄 관련 문서
- 📘 [API 명세서 (Notion)](https://www.notion.so/API-15be63ca457b815ea1c1e595615a27a2)
- 📊 [프로젝트 산출물 (Google Slides)](https://docs.google.com/presentation/d/1-G7xTWYEvxLWTMyvDsN9TML6ZjyK_W37D171g1ZZweM/edit?usp=sharing)
- 🗓️ [데일리 스크럼 기록 (Notion)](https://www.notion.so/15be63ca457b81699860cca4b617bcfd?v=15be63ca457b810b8719000cfcbee85f)
<br>

## 🛠️ 사용 기술

| 카테고리 | 라이브러리 | 설명 |
| :--- | :--- | :--- |
| **Framework**       | Spring Boot           | Java 기반 백엔드 프레임워크 |
|                     | Spring Security               | 인증/인가 및 보안 설정 |
|                     | Spring Data JPA               | ORM 기반 DB 연동 |
|                     | Resilience4j                  | 외부 API 장애 격리용 서킷 브레이커 |
| **Security**        | JWT (io.jsonwebtoken)         | Access Token 기반 인증 |
|                     | Refresh Token + Redis         | HttpOnly 쿠키 + User-Agent 기반 상태 관리 |
| **Database**        | MySQL                         | 주요 관계형 데이터베이스 |
|                     | Redis                         | 토큰/세션 캐시 및 실시간 처리 |
| **Front-End**       | Next.js (v14)                 | React 기반 SSR 프레임워크 |
|                     | React                         | UI 컴포넌트 및 상태 관리 |
|                     | Zustand                       | 전역 상태 관리 라이브러리 |
|                     | Auth.js                       | OAuth2 기반 인증 처리 |
| **Infrastructure**  | AWS Elastic Beanstalk         | Spring Boot 배포 인프라 |
|                     | AWS RDS (MySQL)               | 운영 DB |
|                     | AWS S3                        | 정적 파일 및 객체 저장 |
|                     | AWS CloudFront                | CDN 캐싱 처리 |
|                     | AWS SQS                       | 비동기 메시지 처리용 큐 |
|                     | Vercel                        | Next.js 앱 배포 플랫폼 |
|                     | GitHub Actions                | CI/CD 자동화 워크플로우 |
| **Package & Build** | NPM                           | 프론트엔드 패키지 관리 |
|                     | Gradle                        | 백엔드 의존성 관리 및 빌드 도구 |

<br>


## 📜 아키텍처

CREWS는 서비스의 역할과 책임을 명확히 분리하기 위해 비금융 서비스와 핵심 금융 기능을 독립된 서버로 구축

![mama](https://github.com/user-attachments/assets/bdb54032-e7c4-4390-b4ae-6b7694ee4bb1)


### 📌 프론트 서버 (Vercel + Next.js)

- 정적 자산 배포 및 클라이언트 사이드 렌더링을 담당
- API 요청은 Vercel 프록시를 통해 Spring 기반 백엔드 서버로 전달
- 사용자 인터페이스(UI)와 사용자 경험(UX)을 빠르게 전달하기 위해 전역 CDN 환경(Vercel Edge Network + CloudFront)을 활용

---

### 📌 서비스 서버 (Spring Boot - Elastic Beanstalk)

- 사용자 인증/인가, 모임/게시글 관리, 알림 등 비즈니스 로직의 중심 역할 담당
- 클라이언트로부터 받은 요청 중 금융 처리가 필요한 경우, 내부적으로 BaaS 서버에 API 요청을 위임
- 장애 발생 시에도 서비스 가용성을 확보하기 위해 `Resilience4j` 기반 서킷 브레이커 패턴 적용

---

### 📌 BaaS 서버 (Spring Boot - Elastic Beanstalk)

- 금융 기능만을 독립적으로 담당하는 Core Banking Server
- 주요 기능:
  - 계좌 생성, 삭제, 조회
  - 잔액 변경(입출금), 이체 트랜잭션 관리
- 민감한 금융 로직과 데이터를 외부로부터 분리함으로써 보안성 강화

---

### 📌 RDS (MySQL) - 데이터베이스 분리

- **RDS Service DB**: 서비스 서버 전용 데이터베이스
  - 사용자 정보, 모임, 게시글, 알림, 인증 기록 등 관리
- **RDS BaaS DB**: BaaS 서버 전용 데이터베이스
  - 계좌, 잔액, 거래 기록, 금융 관련 로그 등 보관

> 📎 DB 수준에서도 분리되어 있어, 하나의 시스템에 장애가 발생하더라도 나머지 시스템은 독립적 운영 가능

---

### 📌 AWS SQS (Simple Queue Service)

- 결제 완료 시점을 예측할 수 없기 때문에 비동기 메시지 큐 방식 처리
- 서버 간 직접 호출이 아닌, 큐를 통한 이벤트 기반 처리를 통해 장애 전파를 차단

---

### 📌 Amazon S3 + CloudFront (정적 파일 저장소 + CDN)

- 이미지, 영수증, 첨부 파일 등 미디어 리소스는 S3에 저장/관리
- CloudFront를 통해 어디서든 빠른 다운로드 및 캐시 최적화된 정적 파일 제공 가능

---

### 📌 WebClient + Resilience4j 기반 서버 간 통신 보호

- Service ↔ BaaS 서버 간 통신은 WebClient 기반 처리
- 장애가 감지되면 `Resilience4j Circuit Breaker`가 작동하여 fallback을 수행
- API 호출 실패가 전체 시스템으로 확산되는 상황 차단


<br>



## ✨ 주요 기능

- **통합 대시보드:** 모임의 자산, 다가오는 일정, 회비 납부 현황을 한눈에 파악합니다.
- **회비 관리 자동화:** 정기 회비 납부 요청 및 알림을 자동화하고, 납부 내역을 실시간으로 추적합니다.
- **모임 통장 서비스:** 플랫폼 내에서 간편하게 모임 통장을 개설하고, 회비 이체 및 자산 관리를 수행합니다.
- **활동 및 정산 관리:** 모임 활동 내역을 기록하고, 비용을 투명하게 정산 및 관리합니다.
- **보안:** JWT 및 API Key 기반의 강력한 인증/인가 체계를 통해 사용자의 자산을 안전하게 보호합니다.

<br>


## 📝 주요 사항

### 서버 아키텍처 및 책임 분리
초기 기획 단계에서 **금융 기능(BaaS Core)** 과 **일반 서비스 기능(Service)** 이 하나의 시스템에 통합될 경우, 보안, 장애 전파, 유지보수 측면에서 리스크가 크다고 판단했습니다.
- 애초부터 역할이 다른 두 기능을 분리하고 기능 개발시에도 서로 영향을 주지 않도록 모듈화된 구조로 아키텍처를 설계해야 했습니다.
- 서비스 개발 초기부터 **Crews-BE-Core(금융 기능)**과 **Crews-BE-Service(비금융 서비스)**를 서버 차원에서 분리하였습니다.
- 각 서버는 개별로 배포되며, 역할에 따라 독립적인 인증 구조 및 데이터 모델을 갖도록 설계하였습니다.
    <img width="410" height="112" alt="스크린샷 2025-07-24 오후 4 50 19" src="https://github.com/user-attachments/assets/af10e882-c1ab-4bff-8950-b01dacd00961" />
    >보안상 민감한 금융 처리 로직을 외부에 노출하지 않아 보안성 확보

---

<br>   

### 서버 간 통신 및 보안 구조
서비스 서버와 금융 서버가 분리되어 있었지만, 안전한 인증 및 효율적인 통신 방식이 미정인 상태였습니다.   
특히 금융 기능은 보안이 중요한 만큼, 외부 노출을 최소화하면서 신뢰할 수 있는 방식으로만 접근 가능해야 했습니다.
- 금융 서버는 오직 인증된 서비스 서버와 인증된 사용자의 요청만 수용해야 했습니다.
- 인증 정보는 유출되더라도 쉽게 위변조할 수 없도록 구성해야 했습니다.

- 금융 서버 측에는 핀테크 인증번호 (fintechUseNum)와 API Key를 쌍으로 사용하는 시크릿 키 기반 인증 구조를 추가했습니다.
    - fintechUseNum: 사용자별로 발급되는 고유 식별자
    - API Key: 서비스 서버를 식별하는 고유 키로, 사전에 금융 서버에 등록된 값
- 금융 서버는 요청을 수신하면 이 두 값을 함께 검증하여,
	1.	요청이 서비스 서버로부터 온 요청인지
	2.	요청 사용자가 실제 등록된 사용자인지 확인한 후에만 처리합니다.
> 서비스 서버의 인증된 사용자만이 금융 기능을 사용할 수 있도록 제한함으로써, 민감 데이터의 오용을 원천적으로 차단할 수 있었습니다


---

<br>   

### 데이터 정합성 및 동시성 제어 전략

**문제 인식**  
이체 요청이 동시에 발생하는 상황에서, 계좌 잔액이 올바르게 갱신되지 않는 데이터 정합성 오류가 발생할 수 있음을 발견했습니다.

> 예시 시나리오  
> 계좌 A의 잔액이 10,000원일 때  
> - 스레드 A: 1,000원 출금 요청 → 처리 전 잔액: 10,000  
> - 스레드 B: 1,000원 출금 요청 → 처리 전 잔액: 10,000  
> → 최종 잔액이 9,000으로 덮어써짐 (정상은 8,000)

---

**원인 분석**  
- 트랜잭션 간 동시 접근 제어 미흡
- BigDecimal은 불변이지만 참조 갱신 시점이 충돌
- 계좌 접근 순서가 불일치할 경우 데드락 발생 가능

---

**대안 비교**

| 전략 | 장점 | 단점 |
|------|------|------|
| 낙관적 락 | 데드락 없음 | 충돌 시 롤백 빈번 → 이체에 부적합 |
| 비관적 락 | 정합성 보장 | 데드락 가능성  |
| 작업 큐 처리 | 동시성 완전 제거 | 실시간 처리 불가, SPOF 발생 우려 |
| 분산 락 | 다중 노드 제어 | 단일 노드에 과도, 구현 복잡 |

---


**최종 선택 및 적용 방식**

- `비관적 락(PESSIMISTIC_WRITE)`을 사용하여 잔액 수정 시 동시 접근 제어
```java
    // 수정 작업을 위한 조회 (JPQL + 비관 락 적용)
    @Query("""
            SELECT a
            FROM Account a
            WHERE a.fintechUseNum = :fintechUseNum
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {"customer", "bank"})
    Optional<Account> findByFintechUseNumWithLock(@Param("fintechUseNum") String fintechUseNum);

    @Query("""
            SELECT a
            FROM Account a
            WHERE a.accountNumber = :accountNumber
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(attributePaths = {"customer", "bank"})
    Optional<Account> findByAccountNumberWithLock(@Param("accountNumber") String accountNumber);
```
- 동일 트랜잭션에서 계좌를 접근할 때 출금 계좌 → 입금 계좌 순서로 조회하도록 표준화. (TransactionExecutionService)
```java
 @Transactional(timeout = 10)
public TransferResponse execute(TransferRequest transferRequest) {
    // 계좌 락 점유
    Account withdrawAccount = accountRepository.findByAccountNumberWithLock(transferRequest.getRecvAccountNum())
            .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
    Account depositAccount = accountRepository.findByFintechUseNumWithLock(transferRequest.getFinUseNum())
            .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));


    // 금액 차감 및 증가
    withdrawAccount.subtractFromBalance(transferRequest.getAmt());
    depositAccount.addToBalance(transferRequest.getAmt());
    // 거래 내역 생성
    TransactionHistory[] histories = transactionHistoryService.issueHistory(transferRequest, withdrawAccount, depositAccount, StatusType.SUCCESS);
    TransactionHistory withdrawHistory = histories[0];
    TransactionHistory depositHistory = histories[1];
    accountRepository.saveAll(List.of(withdrawAccount, depositAccount));
    log.info("withdraw & deposit - amount: {} withdraw balance: {}, deposit balance: {}", transferRequest.getAmt(), withdrawAccount.getBalance(), depositAccount.getBalance());

    log.info("success transfer - update history-status");
    return TransferResponse.builder()
            .historyId(withdrawHistory.getCoreTransaction().getId())
            .recvName(depositHistory.getAccount().getCustomer().getName())
            .recvBankcode(depositHistory.getAccount().getBank().getBankCode())
            .recvAccountNum(depositHistory.getAccount().getAccountNumber())
            .amount(withdrawHistory.getTranAmt())
            .afterAmt(withdrawHistory.getAccount().getBalance())
            .transactionTime(withdrawHistory.getCreatedAt())
            .build();
}
```
- 어플리케이션 레벨에서 트랜잭션과 MySQL InnoDB 스토리지 엔진에서 잠금 대기 시간을 일치.
      <img width="410" height="164" alt="스크린샷 2025-07-24 오후 5 47 35" src="https://github.com/user-attachments/assets/1bf538c2-d2e2-4dbf-83fb-4d0cecdf998b" />

- `BigDecimal` 연산 로직은 `synchronized` 키워드로 멀티스레드 충돌 방지 (Acount)
```java
    public synchronized void subtractFromBalance(BigDecimal amt) {
        if (this.balance.compareTo(amt) < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE); // 잔액 부족 예외
        }
        this.balance = this.balance.subtract(amt);
    }

    public synchronized void addToBalance(BigDecimal amt) {
        this.balance = this.balance.add(amt);
    }
```


**성능 및 정합성 검증**

- `ExecutorService + CyclicBarrier` 기반 동시 요청 시뮬레이션 테스트
  ```java
      @Test
    void testConcurrentTransfers() throws Exception {
        int ammountMockA = 12_000_000;
        addMockAccount("mock-finuse-a", BigDecimal.valueOf(ammountMockA)); // A 계좌: 잔액 30만 원
        addMockAccount("mock-finuse-b", BigDecimal.ZERO);           // B 계좌: 잔액 0원

        // 동시성 테스트: A → B로 10번 동시 이체
        int threadCount = 1000;

        Runnable task = () -> {
            try {
                TransferRequest request = new TransferRequest("mock-finuse-a", "mock-finuse-b", transferAmount, "동시성 테스트");
                coreTransactionService.transfer(request);
            } catch (Exception e) {
                fail("Exception occurred: " + e.getMessage());
            }
        };

        runConcurrentTasks(task, threadCount, 1000);

        // 잔액 검증
        Account fromAccount = mockAccounts.get("mock-finuse-a");
        Account toAccount = mockAccounts.get("mock-finuse-b");
        assertEquals(BigDecimal.valueOf(ammountMockA - (threadCount * 10000)), getPrivateBalance(fromAccount), "A 계좌 잔액이 올바르지 않습니다.");
        assertEquals(BigDecimal.valueOf(10_000 * threadCount), getPrivateBalance(toAccount), "B 계좌 잔액이 올바르지 않습니다.");
    }
  ```
  > 1,000건 이상 요청에도 **데이터 오류 없음**


---

<br>   

### 조회 성능 개선 - DB 인덱싱 도입

**문제 인식**  
- 핀테크 이용번호(`fintec_number`) 및 계좌번호(`account_number`) 기반으로 자주 조회되는 쿼리가 존재했으나, 해당 컬럼에 인덱스가 없어 조회 성능 저하 발생.

**해결 방법**  
- `fintec_number`, `account_number` 컬럼에 단일 인덱스 생성  
- 조회 속도 개선을 위한 기본적인 튜닝 전략 적용

**적용 전/후 비교**

| 구분     | 적용 전 (ms) | 적용 후 (ms) | 개선율 (%) |
|----------|--------------|--------------|-------------|
| 평균 응답 속도 | 297          | 213          | 28% 향상     |
| 최대 응답 속도 | 94           | 71           | 24% 향상     |
| 최소 응답 속도 | 40           | 38           | 5% 향상      |

**시각화 결과**  
<img src="https://github.com/user-attachments/assets/1d58a148-0feb-4423-baf6-86c72fb408e6" width="600"/>


> 적용 결과, 평균 28% 이상의 응답 속도 향상을 달성하여 데이터 접근 병목 현상이 크게 완화되었습니다.


---

<br>   

## 장애 전파 차단을 위한 서킷 브레이커 패턴 도입 과정

### 문제 인식

서비스 서버는 금융 데이터를 관리하는 별도의 코어 모듈(BaaS Core)과 분리되어 있으며, 주기적으로 코어 API를 호출해 사용자 계좌 정보를 가져옵니다. 그러나 코어 모듈이 일시적으로 과부하 상태이거나 배포 중일 경우, 다음과 같은 위험이 발생합니다:

- 서비스 모듈이 대기 상태에 빠지며 응답이 지연됨
- 하나의 API 장애가 전체 서비스 장애로 확산되는 **Cascading Failure** 발생 가능

---

### 해결 과제

- **외부 또는 코어 모듈의 장애를 격리**하여 서비스 전체의 안정성을 보장
- 사용자는 최소한의 안내 또는 캐시 응답을 받아 경험 손실 최소화
- 시스템은 장애 복구를 위한 여유를 확보하고 자동 복구 시도 가능해야 함

---

### 기술 선택 및 기반 구성

- **Resilience4j** 라이브러리를 도입하여 서킷 브레이커 패턴 구현
- **Spring Cloud Circuit Breaker** 어노테이션 방식으로 연동
- 장애 발생 시 대체 로직 수행을 위한 `fallbackMethod` 함께 정의

```java
@CircuitBreaker(
  name = "coreService", 
  fallbackMethod = "fallbackFindCoreSideAccounts"
)
public List<AccountResponse> findCoreSideAccounts(MemberToCoreRequest memberDto) {
    // WebClient로 외부 API 호출
    AccountsInfoResponse response = webClient.post()
        .uri("/v1/accounts/info")
        .headers(headers -> {
            headers.set("ACCESS_KEY", accessKey);
            headers.set("SECRET_KEY", secretKey);
        })
        .bodyValue(memberDto)
        .retrieve()
        .bodyToMono(AccountsInfoResponse.class)
        .block();

    return response.toAccounts();
}
```

### Fallback
- 장애 발생 원인을 로그로 남기고 사용자에게는 서버 에러 응답을 명확하게 전달
```java
public List<AccountResponse> fallbackFindCoreSideAccounts(
    MemberToCoreRequest request, Throwable throwable) {

    log.error("Fallback triggered for findCoreSideAccounts. Request: {}, Reason: {}", 
              request, throwable.getMessage());

    throw new CustomException(ErrorCode.CORE_SERVER_EXCEPTION);
}
```


---

<br>   

## 🔖 커밋 메시지 컨벤션

| 태그       | 설명                                      |
|------------|-------------------------------------------|
| feat       | 새로운 기능 추가                         |
| fix        | 버그 수정                                 |
| docs       | 문서 수정 (README 등)                    |
| refactor   | 코드 리팩터링 (기능 변경 없이 구조 개선) |
| test       | 테스트 코드 추가 또는 리팩터링           |
| rename     | 파일 또는 폴더명을 수정/이동한 경우      |
| remove     | 파일 삭제 작업만 수행한 경우             |
| !HOTFIX    | 서비스에 영향을 주는 치명적 버그 긴급 수정 |
