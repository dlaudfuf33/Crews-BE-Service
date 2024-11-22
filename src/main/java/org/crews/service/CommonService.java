package org.crews.service;

import lombok.RequiredArgsConstructor;
import org.crews.dto.core.AccountInfoResponse;
import org.crews.dto.core.AccountIssuedResponse;
import org.crews.dto.core.CIOnlyRequest;
import org.crews.dto.request.DateRequest;
import org.crews.dto.response.*;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.AccountType;
import org.crews.repository.AccountRepository;
import org.crews.repository.DuesRepository;
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
    private final AgitService agitService;
    private final DuesRepository duesRepository;

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
                DuesAlarmResponse duesAlarm = agitService.getDuesAlarm(agit.getId(), memberId);
                accountV2CrewResponses.add(AccountV2CrewResponse.of(agit, duesAlarm.getDuesAmount()));
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

    public AccountHistoryFinalV2Response getMyAccountsHistory(Long memberId, DateRequest dateRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        List<Membership> membershipList = memberShipRepository.findByMember(member);
        List<AccountHistoryV2Response> list = new ArrayList<>();
        for(Membership membership : membershipList){
            List<Dues> duesList = membership.getDuesList();
            if(duesList.isEmpty()) continue;
            List<Dues> filterDues = duesList.stream().filter(content ->
                            (content.getDueDate().getMonthValue() == dateRequest.getMonth()) && (content.getDueDate().getYear() == dateRequest.getYear()))
                    .toList();
            List<AccountHistoryV2Dto> accountHistoryV2DtoList = filterDues.stream().map(dues -> AccountHistoryV2Dto.of(dues, membership.getAgit().getId())).toList();
            list.add(AccountHistoryV2Response.builder().accountHistory(accountHistoryV2DtoList).build());
        }
        return AccountHistoryFinalV2Response.builder().finalAccountHistory(list).build();


    }
}
