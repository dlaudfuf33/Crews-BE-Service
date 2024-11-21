package org.crews.service;

import lombok.RequiredArgsConstructor;
import org.crews.dto.core.AccountInfoResponse;
import org.crews.dto.core.AccountIssuedResponse;
import org.crews.dto.core.CIOnlyRequest;
import org.crews.dto.response.AccountV2CrewResponse;
import org.crews.dto.response.AccountV2PersonalResponse;
import org.crews.dto.response.AccountV2Response;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.Account;
import org.crews.model.Agit;
import org.crews.model.Member;
import org.crews.model.Membership;
import org.crews.model.constants.AccountType;
import org.crews.repository.AccountRepository;
import org.crews.repository.MemberRepository;
import org.crews.repository.MemberShipRepository;
import org.crews.utils.AESUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommonService {
    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final MemberShipRepository memberShipRepository;
    private final CoreService coreService;

    @Transactional
    public AccountV2Response getAllAccounts(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );

        List<Agit> agitList = memberShipRepository.findByMember(member).stream().map(Membership::getAgit).toList();
        List<AccountV2CrewResponse> accountV2CrewResponses = new ArrayList<>();
        if (!agitList.isEmpty()) {
            for (Agit agit : agitList) {
                if (agit.getAgitAndAccount() == null)
                    continue;
                accountV2CrewResponses.add(AccountV2CrewResponse.from(agit));
            }
        }
        AccountInfoResponse allAccounts = coreService.getAllAccounts(CIOnlyRequest.builder().ci(member.getCi()).build());
        for(AccountIssuedResponse response : allAccounts.getAccountList()){
            Optional<Account> findAccount = accountRepository.findByAccountNumber(AESUtil.encrypt(response.getAccountNumber()));
            if(findAccount.isEmpty()) continue;
            findAccount.get().setBalance(response.getBalance());
        }

        List<Account> accountList = accountRepository.findByMemberAndAccountType(member, AccountType.PERSONAL);
        List<AccountV2PersonalResponse> accountV2PersonalResponses = accountList.stream().map(AccountV2PersonalResponse::from).toList();

        return AccountV2Response.builder().crewAccountList(accountV2CrewResponses).personalAccountList(accountV2PersonalResponses).build();


    }
}
