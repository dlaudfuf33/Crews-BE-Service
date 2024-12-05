package org.crews.service;

import lombok.RequiredArgsConstructor;
import org.crews.dto.core.AccountInfoResponse;
import org.crews.dto.core.AccountIssuedResponse;
import org.crews.dto.core.CIOnlyRequest;
import org.crews.dto.response.*;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.AccountType;
import org.crews.repository.*;
import org.crews.utils.AESUtil;
import org.crews.utils.MaskedNumber;
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
    private final MembershipRepository membershipRepository;
    private final BankRepository bankRepository;
    private final CoreService coreService;
    private final AgitService agitService;

    @Transactional
    public AccountV2Response getAllAccounts(Long memberId, Integer year, Integer month) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );

        List<Agit> agitList = membershipRepository.findByMember(member).stream().map(Membership::getAgit).toList();
        List<AccountV2CrewResponse> accountV2CrewResponses = new ArrayList<>();
        if (!agitList.isEmpty()) {
            for (Agit agit : agitList) {
                if (agit.getAgitAndAccount() == null)
                    continue;
                DuesAlarmResponse duesAlarm = agitService.getDuesAlarm(agit.getId(), memberId, year, month);
                accountV2CrewResponses.add(AccountV2CrewResponse.of(agit, duesAlarm.getDueAmount()));
            }
        }
        AccountInfoResponse allAccounts = coreService.getAllAccounts(CIOnlyRequest.builder().ci(member.getCi()).build());
        for(AccountIssuedResponse response : allAccounts.getAccountList()){
            Optional<Account> findAccount = accountRepository.findByAccountNumber(AESUtil.encrypt(response.getAccountNumber()));
            if(findAccount.isEmpty()) {
                Bank bank = bankRepository.findByBankCode(response.getBankCode()).orElseThrow(
                        () -> new CustomException(ErrorCode.BANK_NOT_FOUND)
                );
                Account account = Account.builder().bank(bank).member(member).maskedAccountNumber(MaskedNumber.maskedAccountNumber(response.getAccountNumber()))
                        .accountNumber(AESUtil.encrypt(response.getAccountNumber())).balance(response.getBalance()).
                        accountType(response.getAccountType()).fintecNumber(response.getFintechUseNum()).productName(response.getProductName()).build();
                accountRepository.save(account);
                continue;
            }
            findAccount.get().setBalance(response.getBalance());
        }

        List<Account> accountList = accountRepository.findByMemberAndAccountType(member, AccountType.PERSONAL);
        List<AccountV2PersonalResponse> accountV2PersonalResponses = accountList.stream().map(AccountV2PersonalResponse::from).toList();
        return AccountV2Response.builder().crewAccountList(accountV2CrewResponses).personalAccountList(accountV2PersonalResponses).build();
    }

    public AccountHistoryFinalV2Response getMyAccountsHistory(Long memberId, Integer year, Integer month) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        List<Membership> membershipList = membershipRepository.findByMember(member);
        List<AccountHistoryResponse> list = new ArrayList<>();
        for(Membership membership : membershipList){
            List<Dues> duesList = membership.getDuesList();
            if(duesList.isEmpty()) continue;
            List<Dues> filterDues = duesList.stream().filter(content ->
                            (content.getStandardDate().getMonthValue() == month) && (content.getStandardDate().getYear() == year))
                    .toList();
            List<AccountHistoryResponse> accountHistoryResponse = filterDues.stream().map(dues -> AccountHistoryResponse.of(dues, membership.getAgit().getId())).toList();
            list.addAll(accountHistoryResponse);
        }
        list.sort((o1, o2) -> o2.getDueDate().compareTo(o1.getDueDate()));
        return AccountHistoryFinalV2Response.builder().accountHistory(list).build();


    }
}
