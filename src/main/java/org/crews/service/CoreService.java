package org.crews.service;

import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.*;
import org.crews.dto.request.TransactionDetailRequest;
import org.crews.dto.response.AccountsInfoResponse;
import org.crews.dto.response.ProductAllResponse;
import org.crews.dto.response.TransactionDetailResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class CoreService {
    private static final String HEADER_ACCESS_KEY = "X-ACCESS-KEY";
    private static final String HEADER_SECRET_KEY = "X-SECRET-KEY";
    private static final String CALL_FAILURE_MESSAGE = "호출 실패: ";
    private static final String WEBCLIENT_COMMUNICATION_ERROR = "WebClient 통신중 오류 발생: ";

    private static final String INITAL_ACCOUNT = "/v1/accounts/info/init";
    private static final String REFRESH_BALANCE = "/v1/accounts/info/balance";
    private static final String ISUUE_MULTIFINTECHNUM = "/v1/accounts/fin-nums";

    private final WebClient webClient;

    private final String accessKey;

    private final String secretKey;

    // 생성자를 통해 의존성을 주입받음
    public CoreService(
            @Value("${core.api.core-url}") String coreUrl,
            @Value("${bank.core.access-key}") String accessKey,
            @Value("${bank.core.secret-key}") String secretKey) {
        this.webClient = WebClient.builder()
                .baseUrl(coreUrl) // baseUrl 주입
                .build();
        this.accessKey = accessKey; // accessKey 주입
        this.secretKey = secretKey; // secretKey 주입
    }

    // POST 요청 - 블로킹 방식
    public String postTestBlocking(MemberToCoreRequest customer) {
        try {
            log.info("블로킹 방식 API 호출 시작 - Access Key: {}", accessKey);
            return webClient.post()
                    .uri("/test-api/block")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(customer)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();  // 블로킹 방식으로 Mono 값을 얻음
        } catch (WebClientResponseException e) {
            log.error(CALL_FAILURE_MESSAGE, e);
            return CALL_FAILURE_MESSAGE + e.getMessage();
        }
    }

    // POST 요청 - 논블로킹 방식
    public Mono<String> postTestNonBlocking(MemberToCoreRequest customer) {
        log.info("논블로킹 방식 API 호출 시작 - Access Key: {}, ScretKey : {}", accessKey, secretKey);

        return webClient.post()
                .uri("/test-api/nonblock")
                .headers(headers -> {
                    headers.set(HEADER_ACCESS_KEY, accessKey);
                    headers.set(HEADER_SECRET_KEY, secretKey);
                })
                .bodyValue(customer)
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)) // 재시도 로직 설정
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            log.info("재시도 횟수 초과. 마지막 오류: {}", retrySignal.failure().getMessage());
                            return retrySignal.failure();
                        })
                )
                .doOnError(e -> log.error(WEBCLIENT_COMMUNICATION_ERROR, e));
    }


    // 사용자의 등록 계좌 잔액 갱신 - 블로킹 방식
    public List<FintechBalancePairResponse> balanceLoad(BalanceRequest balanceRequest) {
        try {
            log.info("등록 계좌 잔액 갱신 - AccessKey: {}, SecretKey: {}", accessKey, secretKey);
            validateKeys();

            // WebClient 호출 및 응답 처리
            List<FintechBalancePairResponse> response = webClient.post()
                    .uri(REFRESH_BALANCE)
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(balanceRequest)
                    .retrieve()
                    .bodyToFlux(FintechBalancePairResponse.class) // 리스트 형태로 응답 매핑
                    .collectList() // Mono<List<FintechBalancePairResponse>>로 변환
                    .block(); // 블로킹 방식

            // 응답이 null이거나 비어 있는 경우 처리
            if (response == null || response.isEmpty()) {
                log.warn("응답이 null이거나 빈 리스트입니다.");
                return List.of();
            }

            // 로깅: 총 수신된 데이터 건수
            log.info("응답 데이터 수신 완료: 총 {}건 처리", response.size());
            return response;

        } catch (IllegalArgumentException e) {
            log.error("필수 인자가 누락되었습니다: {}", e.getMessage());
            throw new CustomException(ErrorCode.REQUIRED_NOT_NULL, e);

        } catch (WebClientResponseException e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.UNKNOWN_EXCEPTION, e);
        }
    }

    // 사용자의 모든 계좌 조회 - 블로킹 방식
    public List<AccountResponse> findCoreSideAccounts(MemberToCoreRequest memberDto) {
        try {
            // Null 체크
            if (memberDto == null || memberDto.getCi() == null) {
                log.info("입력 값이 null: {}", memberDto);
                throw new CustomException(ErrorCode.REQUIRED_NOT_NULL);
            }

            log.info("블로킹 방식 모든 계좌 호출 시작 - AccessKey: {}, SecretKey: {}", accessKey, secretKey);
            validateKeys();

            // WebClient 호출 및 응답 처리
            AccountsInfoResponse response = webClient.post()
                    .uri(INITAL_ACCOUNT)
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(memberDto)
                    .retrieve()
                    .bodyToMono(AccountsInfoResponse.class) // 응답 매핑
                    .block(); // 블로킹 방식

            if (response == null || response.getAccounts() == null) {
                log.warn("응답이 null입니다. 빈 리스트를 반환합니다.");
                return List.of();
            }

            log.info("응답 데이터 수신 완료: 총 {}건 중 {}건 처리", response.getTotalCount(), response.getAccounts().size());
            return response.getAccounts().stream()
                    .map(AccountResponse::new)
                    .toList();

        } catch (IllegalArgumentException e) {
            log.error("필수 인자가 누락되었습니다: {}", e.getMessage());
            throw new CustomException(ErrorCode.REQUIRED_NOT_NULL, e);

        } catch (WebClientResponseException e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.REQUIRED_NOT_NULL, e);

        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.UNKNOWN_EXCEPTION, e);
        }

    }


    // 사용자의 계좌 등록 - 블로킹 방식
    public List<AttachResponse> attachAccount(FintechNumRequest fintechNumRequest) {
        try {
            if (fintechNumRequest == null || fintechNumRequest.getCi() == null || fintechNumRequest.getAccountNumbers() == null) {
                log.info("입력 값이 null: {}", fintechNumRequest);
                throw new CustomException(ErrorCode.REQUIRED_NOT_NULL);
            }

            log.info("블로킹 방식 모든 계좌 호출 시작 - AccessKey: {}, SecretKey: {}", accessKey, secretKey);
            validateKeys();

            AccountsInfoResponse response = webClient.post()
                    .uri(ISUUE_MULTIFINTECHNUM)
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(fintechNumRequest)
                    .retrieve()
                    .bodyToMono(AccountsInfoResponse.class)
                    .block();

            if (response == null || response.getAccounts().isEmpty()) {
                log.warn("응답이 null이거나 계좌가 없습니다. 빈 리스트를 반환합니다.");
                return List.of();
            }

            log.info("응답 데이터 수신 완료: 총 {}건 중 {}건 처리", response.getAccounts().get(0).getAccountNumber(), response.getAccounts().get(0).getFintechUseNum());
            log.info("응답 데이터 수신 완료: 총 {}건 중 {}건 처리", response.getTotalCount(), response.getAccounts().size());

            // AttachResponse 리스트 반환
            return response.getAccounts().stream()
                    .<AttachResponse>map(accountInfo -> AttachResponse.builder()
                            .customerName(accountInfo.getCustomerName())
                            .bankCode(accountInfo.getBankCode())
                            .bankImage(accountInfo.getBankImage())
                            .productName(accountInfo.getProductName())
                            .accountNumber(accountInfo.getAccountNumber())
                            .accountType(accountInfo.getAccountType())
                            .balance(accountInfo.getBalance())
                            .createdAt(accountInfo.getCreatedAt())
                            .updatedAt(accountInfo.getUpdatedAt())
                            .fintechUseNum(accountInfo.getFintechUseNum())
                            .build()
                    )
                    .toList();

        } catch (IllegalArgumentException e) {
            log.error("필수 인자가 누락되었습니다: {}", e.getMessage());
            throw new CustomException(ErrorCode.REQUIRED_NOT_NULL, e);

        } catch (WebClientResponseException e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.CORE_RESPONSE_ERROR, e);

        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.UNKNOWN_EXCEPTION, e);
        }
    }

    public AccountOneResponse accountInfo(CommonRequest commonRequest) {
        try {
            return webClient.post()
                    .uri("/v1/accounts/one")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(commonRequest) //
                    .retrieve()
                    .bodyToMono(AccountOneResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.warn(WEBCLIENT_COMMUNICATION_ERROR + "{}", ex.getMessage());
            throw new WebServerException(ex.getResponseBodyAsString(), ex);
        }
    }

    public Mono<AccountIssuedResponse> sendCICode(CIRequest ciRequest) {
        try {
            return webClient.post()
                    .uri("/v1/ci")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(ciRequest) //
                    .retrieve()
                    .bodyToMono(AccountIssuedResponse.class)
                    .retryWhen(Retry.backoff(3, Duration.ofSeconds(5)) // 재시도 로직 설정
                            .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                                log.info("재시도 횟수 초과. 마지막 오류: {}", retrySignal.failure().getMessage());
                                return retrySignal.failure();
                            })
                    )
                    .doOnError(e -> log.error("API 호출 중 오류 발생", e));
        } catch (WebClientResponseException ex) {
            log.warn(WEBCLIENT_COMMUNICATION_ERROR + "{}", ex.getMessage());
            throw new WebServerException(ex.getResponseBodyAsString(), ex);
        }
    }

    public AccountIssuedResponse accountIssued(ProductInfoRequest productInfoRequest) {
        try {
            AccountIssuedResponse response = webClient.post()
                    .uri("/v1/accounts")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(productInfoRequest) //
                    .retrieve()
                    .bodyToMono(AccountIssuedResponse.class)
                    .block();
            if (response == null)
                throw new CustomException(ErrorCode.WRONG_RESPONSE);
            return response;
        } catch (WebClientResponseException ex) {
            log.warn(WEBCLIENT_COMMUNICATION_ERROR + "{}", ex.getMessage());
            throw new WebServerException(ex.getResponseBodyAsString(), ex);
        }
    }

    public CardIssuedResponse cardIssued(CommonRequest commonRequest) {
        try {
            CardIssuedResponse response = webClient.post()
                    .uri("/v1/cards")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(commonRequest)
                    .retrieve()
                    .bodyToMono(CardIssuedResponse.class)
                    .block();
            if (response == null)
                throw new CustomException(ErrorCode.WRONG_RESPONSE);
            return response;
        } catch (WebClientResponseException ex) {
            log.warn(WEBCLIENT_COMMUNICATION_ERROR + "{}", ex.getMessage());
            throw new WebServerException(ex.getResponseBodyAsString(), ex);
        }
    }

    public MessageResponse cardRemove(CoreCardRemoveRequest coreCardRemoveRequest) {
        try {
            MessageResponse response = webClient.method(HttpMethod.DELETE)
                    .uri("/v1/cards")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(coreCardRemoveRequest)
                    .retrieve()
                    .bodyToMono(MessageResponse.class)
                    .block();
            if (response == null)
                throw new IllegalStateException("잘못된 응답값 입니다.");
            return response;
        } catch (WebClientResponseException ex) {
            log.warn("클라이언트 통신 중 오류가 발생했습니다.{}", ex.getMessage());
            throw new WebServerException(ex.getResponseBodyAsString(), ex);
        }
    }

    public AccountInfoResponse getAllAccounts(CIOnlyRequest ci) {
        try {
            AccountInfoResponse response = webClient.post()
                    .uri("/v1/accounts/info")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(ci)
                    .retrieve()
                    .bodyToMono(AccountInfoResponse.class)
                    .block();
            if (response == null)
                throw new IllegalStateException("잘못된 응답값 입니다.");
            return response;
        } catch (WebClientResponseException ex) {
            log.warn("클라이언트 통신 중 오류가 발생했습니다.{}", ex.getMessage());
            throw new WebServerException(ex.getResponseBodyAsString(), ex);
        }
    }

    public TransactionDetailResponse filteredAccountHistory(TransactionDetailRequest transactionDetailRequest) {
        try {
            TransactionDetailResponse response = webClient.post()
                    .uri("/v1/transfer/details")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(transactionDetailRequest)
                    .retrieve()
                    .bodyToMono(TransactionDetailResponse.class)
                    .block();
            if (response == null)
                throw new IllegalStateException("잘못된 응답값 입니다.");
            return response;
        } catch (WebClientResponseException ex) {
            log.warn("클라이언트 통신 중 오류가 발생했습니다.{}", ex.getMessage());
            throw new WebServerException(ex.getResponseBodyAsString(), ex);
        }
    }

    public TransactionDetailResponse getWithdrawHistory(WithdrawTransactionRequest withdrawTransactionRequest) {
        try {
            TransactionDetailResponse response = webClient.post()
                    .uri("/v1/transfer/withdraws")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(withdrawTransactionRequest)
                    .retrieve()
                    .bodyToMono(TransactionDetailResponse.class)
                    .block();
            if (response == null) {
                throw new IllegalStateException("잘못된 응답값 입니다.");
            }
            return response;
        } catch (WebClientResponseException ex) {
            log.warn("클라이언트 통신 중 오류가 발생했습니다.{}", ex.getMessage());
            throw new WebServerException(ex.getResponseBodyAsString(), ex);
        }
    }

    public TransferApiResponse transferFee(TransferRequest transferRequest) {
        try {
            TransferApiResponse response = webClient.post()
                    .uri("/v1/transfer")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(transferRequest)
                    .retrieve()
                    .bodyToMono(TransferApiResponse.class)
                    .block();
            if (response == null) {
                throw new IllegalStateException("잘못된 응답값 입니다.");
            }
            return response;
        } catch (WebClientResponseException ex) {
            log.warn("클라이언트 통신 중 오류가 발생했습니다.{}", ex.getMessage());
            throw new WebServerException(ex.getResponseBodyAsString(), ex);
        }
    }

    public ProductAllResponse getAllProducts() {
        try {
            ProductAllResponse response = webClient.get()
                    .uri("/v1/products")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .retrieve()
                    .bodyToMono(ProductAllResponse.class)
                    .block();
            if (response == null)
                throw new IllegalStateException("잘못된 응답값 입니다.");
            return response;
        } catch (WebClientResponseException ex) {
            log.warn("클라이언트 통신 중 오류가 발생했습니다.{}", ex.getMessage());
            throw new WebServerException(ex.getResponseBodyAsString(), ex);
        }
    }


    public ApiResponse<TransferResponse> transfer(TransferRequest transferRequest) {
        try {
            return webClient.post()
                    .uri("/v1/transfer")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(transferRequest) //
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<TransferResponse>>() {
                    })
                    .block();
        } catch (WebClientResponseException ex) {
            log.warn(WEBCLIENT_COMMUNICATION_ERROR + "{}", ex.getMessage());
            throw new WebServerException(ex.getResponseBodyAsString(), ex);
        }
    }

    public BalanceInfoResponse getBalanceInfo(BalanceInfoRequest balanceInfoRequest) {
        try {
            return webClient.post()
                    .uri("/v1//balance/info")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(balanceInfoRequest) //
                    .retrieve()
                    .bodyToMono(BalanceInfoResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.warn(WEBCLIENT_COMMUNICATION_ERROR + "{}", ex.getMessage());
            throw new WebServerException(ex.getResponseBodyAsString(), ex);
        }
    }

    private void validateKeys() {
        if (accessKey == null || secretKey == null) {
            throw new CustomException(ErrorCode.REQUIRED_NOT_NULL, "AccessKey 또는 SecretKey가 설정되지 않았습니다.");
        }
    }

    public TransactionDetailResponse DateAccountHistory(AccountInfoOfDate accountInfoOfDate) {
        try {
            TransactionDetailResponse response = webClient.post()
                    .uri("/v1/accounts/info/date")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(accountInfoOfDate)
                    .retrieve()
                    .bodyToMono(TransactionDetailResponse.class)
                    .block();
            if (response == null)
                throw new IllegalStateException("잘못된 응답값 입니다.");
            return response;
        } catch (WebClientResponseException ex) {
            log.warn("클라이언트 통신 중 오류가 발생했습니다.{}", ex.getMessage());
            throw new WebServerException(ex.getResponseBodyAsString(), ex);
        }
    }
}