package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.AccountLinkRequest;
import org.crews.dto.MemberIdDto;
import org.crews.dto.core.AccountIssuedResponse;
import org.crews.dto.core.AccountOneResponse;
import org.crews.dto.core.CommonRequest;
import org.crews.dto.core.IdentityRequest;
import org.crews.model.*;
import org.crews.repository.*;
import org.crews.util.AES;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {
    private final AgitRepository agitRepository;
    private final BankRepository bankRepository;
    private final AccountRepository accountRepository;
    private final MemberShipRepository memberShipRepository;
    private final MemberRepository memberRepository;
    private final AgitAndAccountRepository agitAndAccountRepository;
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

    @Transactional
    public AccountIssuedResponse accountIssued(Long agitId, MemberIdDto memberIdDto) {
        Member member = memberRepository.findById(memberIdDto.getMemberId()).orElseThrow(
                () -> new IllegalStateException("해당하는 번호의 멤버가 없습니다.")
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new IllegalStateException("해당하는 번호의 아지트가 없습니다.")
        );
        Membership membership = memberShipRepository.findByAgitAndRole(agit, MemberRole.LEADER).orElseThrow(
                () -> new IllegalStateException("아지트에 모임장이 존재하지 않습니다.")
        );
        String identityCode = membership.getMember().getIdentityCode();
        if(!member.getIdentityCode().equals(identityCode)){
            throw new IllegalStateException("모임통장을 생성할 수 없습니다.");
        }
        try {
            AccountIssuedResponse response = webClient.post()
                    .uri("/v1/accounts")
                    .headers(headers -> {
                        headers.set(HEADER_ACCESS_KEY, accessKey);
                        headers.set(HEADER_SECRET_KEY, secretKey);
                    })
                    .bodyValue(IdentityRequest.builder().identityCode(identityCode).build()) //
                    .retrieve()
                    .bodyToMono(AccountIssuedResponse.class)
                    .block();
            if(response == null)
                throw new IllegalStateException("잘못된 응답값 입니다.");

            Bank bank = bankRepository.findByBankCode(response.getBankCode()).orElseThrow(
                    () -> new IllegalStateException("잘못된 뱅크코드 입니다.")
            );

            Account account = Account.builder().bank(bank).member(membership.getMember()).maskedAccountNumber(maskedAccountNumber(response.getAccountNumber()))
                    .accountNumber(AES.encrypt_AES(response.getAccountNumber())).balance(response.getBalance()).
                    accountType(response.getAccountType()).fintecNumber(response.getFintechUseNum()).build();
            Account savedAccount = accountRepository.save(account);
            return AccountIssuedResponse.from(savedAccount);
        }
        catch (WebClientException ex){
            log.warn("클라이언트 통신 중 오류가 발생했습니다.");
            throw new WebServerException("클라이언트 통신 중 오류가 발생했습니다.", ex);
        }
    }

    @Transactional
    public AgitAndAccount accountLink(Long agitId, AccountLinkRequest accountLinkRequest) {
        Member member = memberRepository.findById(accountLinkRequest.getMemberId()).orElseThrow(
                () -> new IllegalStateException("해당하는 번호의 멤버가 없습니다.")
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new IllegalStateException("해당하는 번호의 아지트가 없습니다.")
        );
        Membership membership = memberShipRepository.findByAgitAndRole(agit, MemberRole.LEADER).orElseThrow(
                () -> new IllegalStateException("아지트에 모임장이 존재하지 않습니다.")
        );
        String identityCode = membership.getMember().getIdentityCode();
        if(!member.getIdentityCode().equals(identityCode)){
            throw new IllegalStateException("모임통장을 생성할 수 없습니다.");
        }
        Account account = accountRepository.findByFintecNumber(accountLinkRequest.getFintechUseNum()).orElseThrow(
                () -> new IllegalStateException("핀테크 번호에 해당하는 계좌가 없습니다.")
        );
        AgitAndAccount agitAndAccount = AgitAndAccount.builder().account(account).agit(agit).build();
        log.info("{}번의 아지트({})와 모임통장({})이 연결되었습니다.",agitId, agit.getAgitName(), identityCode);
        return agitAndAccountRepository.save(agitAndAccount);


    }

    private String maskedAccountNumber(String accountNumber){
        String maskingResult = "";

        if (accountNumber.length() >= 7) {
            maskingResult = accountNumber.replaceAll("(?<=.{4}).(?=.{2})", "*");
        } else {
            maskingResult = accountNumber;
        }

        return maskingResult;

    }
}
