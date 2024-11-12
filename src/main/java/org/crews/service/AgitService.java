package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.AccountOneResponse;
import org.crews.dto.core.CommonRequest;
import org.crews.model.Agit;
import org.crews.model.AgitAndAccount;
import org.crews.repository.AgitRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgitService {

    private final AgitRepository agitRepository;
    private final WebClient webClient;

    private static final String HEADER_ACCESS_KEY = "X-ACCESS-KEY";
    private static final String HEADER_SECRET_KEY = "X-SECRET-KEY";

    @Value("${bank.core.access-key}")
    private String accessKey;

    @Value("${bank.core.secret-key}")
    private String secretKey;

    @Transactional(readOnly = true)
    public AccountOneResponse accountInfo(Long agitId) {
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new IllegalStateException("해당하는 번호의 아지트가 없습니다.")
        );
        AgitAndAccount agitAndAccount = agit.getAgitAndAccount();
        if(agitAndAccount == null){
            throw new IllegalStateException("해당하는 아지트의 모임통장이 없습니다.");
        }
        String fintecNumber = agitAndAccount.getAccount().getFintecNumber();
        String identityCode = agitAndAccount.getAccount().getMember().getIdentityCode();
        CommonRequest commonRequest = CommonRequest.builder().identityCode(identityCode).fintechUseNum(fintecNumber).build();
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
        }
        catch (WebClientException ex){
            log.warn("클라이언트 통신 중 오류가 발생했습니다.");
            throw new WebServerException("클라이언트 통신 중 오류가 발생했습니다.", ex);
        }
    }
}
