package org.crews.service;

import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.*;
import org.crews.excaption.CustomException;
import org.crews.excaption.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class CoreService {
    private static final String HEADER_ACCESS_KEY = "X-ACCESS-KEY";
    private static final String HEADER_SECRET_KEY = "X-SECRET-KEY";
    private static final String CALL_FAILURE_MESSAGE = "호출 실패: ";
    private static final String WEBCLIENT_COMMUNICATION_ERROR = "WebClient 통신중 오류 발생: ";

    private static final String INITAL_ACCOUNT = "/v1/accounts/info/init";

    private final WebClient webClient;

    private final String accessKey;

    private final String secretKey;

    // 생성자를 통해 의존성을 주입받음
    public CoreService(
            @Value("${core.api.base-url}") String baseUrl,
            @Value("${bank.core.access-key}") String accessKey,
            @Value("${bank.core.secret-key}") String secretKey) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl) // baseUrl 주입
                .build();
        this.accessKey = accessKey; // accessKey 주입
        this.secretKey = secretKey; // secretKey 주입
    }

    // POST 요청 - 블로킹 방식
    public String postTestBlocking(MemberToCoreDto customer) {
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
    public Mono<String> postTestNonBlocking(MemberToCoreDto customer) {
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

    // 사용자의 모든 계좌 조회 - 블로킹 방식
    public List<AccountResponseDto> findCoreSideAccounts(MemberToCoreDto memberDto) {
        try {
            // Null 체크 추가
            if (memberDto == null || memberDto.getName() == null || memberDto.getPhoneNumber() == null) {
                log.info("{}", memberDto);
                throw new CustomException(ErrorCode.REQUIRED_NOT_NULL);
            }
            log.info("블로킹 방식 모든 계좌 호출 시작 - {} / {}", accessKey, secretKey);
            if (accessKey == null || secretKey == null) {
                throw new CustomException(ErrorCode.REQUIRED_NOT_NULL);
            }

            AccountResponseDto[] response = webClient.post()
                    .uri(INITAL_ACCOUNT)
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(memberDto) //
                    .retrieve()
                    .bodyToMono(AccountResponseDto[].class) // 응답을 AccountResponseDto 배열로 매핑
                    .block(); // 블로킹 방식으로 Mono 값을 얻음

            // Null 응답 처리
            if (response == null) {
                log.warn("응답이 null입니다. 빈 리스트를 반환합니다.");
                return List.of(); // 응답이 null일 경우 빈 리스트 반환
            }
            log.info("{}", Arrays.stream(response).toList().get(0));

            return Arrays.asList(response);

        } catch (IllegalArgumentException e) {
            log.error("필수 인자가 누락되었습니다: {}", e.getMessage());
            return List.of(); // 필수 값이 누락된 경우 빈 리스트 반환

        } catch (WebClientResponseException e) {
            log.error("API 호출 중 오류 발생: {}", e.getMessage(), e);
            return List.of(); // 호출 실패 시 빈 리스트 반환

        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
            return List.of(); // 그 외 예기치 못한 오류 처리
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

    public Mono<String> sendCICode(CIRequest ciRequest) {
        try {
            return webClient.post()
                    .uri("/v1/ci")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(ciRequest) //
                    .retrieve()
                    .bodyToMono(String.class)
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

    public AccountIssuedResponse accountIssued(String ci) {
        try {
            AccountIssuedResponse response = webClient.post()
                    .uri("/v1/accounts")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(CIRequest.builder().ci(ci).build()) //
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
            if(response == null)
                throw new IllegalStateException("잘못된 응답값 입니다.");
            return response;
        }
        catch (WebClientResponseException ex){
            log.warn("클라이언트 통신 중 오류가 발생했습니다.{}", ex.getMessage());
            throw new WebServerException(ex.getResponseBodyAsString(), ex);
        }
    }
}