package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.*;
import org.crews.dto.request.AccountDetailsRequest;
import org.crews.dto.request.AccountLinkRequest;

import org.crews.dto.core.AccountIssuedResponse;
import org.crews.dto.core.AccountOneResponse;
import org.crews.dto.core.CommonRequest;

import org.crews.dto.request.TransactionDetailRequest;
import org.crews.dto.response.AccountLinkResponse;
import org.crews.dto.response.TransactionDetailResponse;
import org.crews.dto.request.MemberIdRequest;
import org.crews.dto.response.TransactionHistoryResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.MemberRole;
import org.crews.model.constants.TranType;
import org.crews.repository.*;
import org.crews.utils.AESUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private final CoreService coreService;

    @Transactional
    public AccountOneResponse accountInfo(Long agitId) {
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        AgitAndAccount agitAndAccount = agit.getAgitAndAccount();
        if (agitAndAccount == null) {
            return AccountOneResponse.builder().build();
        }
        String fintecNumber = agitAndAccount.getAccount().getFintecNumber();
        String ci = agitAndAccount.getAccount().getMember().getCi();
        CommonRequest commonRequest = CommonRequest.builder().ci(ci).fintechUseNum(fintecNumber).build();
        AccountOneResponse accountOneResponse = coreService.accountInfo(commonRequest);
        accountOneResponse.setAccountId(agitAndAccount.getAccount().getId());
        agitAndAccount.getAccount().setBalance(accountOneResponse.getBalance());
        return accountOneResponse;
    }

    @Transactional
    public AccountIssuedResponse accountIssued(Long agitId, MemberIdRequest memberIdRequest, MemberRole memberRole) {
        Member member = memberRepository.findById(memberIdRequest.getMemberId()).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        Membership membership = memberShipRepository.findByAgitAndRole(agit, memberRole).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_ACCOUNT_NOT_FOUND)
        );
        String ci = membership.getMember().getCi();
        if (!member.getCi().equals(ci)) {
            throw new CustomException(ErrorCode.AUTHORIZED_ACCOUNT_CREATION);
        }
        AccountIssuedResponse response = coreService.accountIssued(ci);
        Bank bank = bankRepository.findByBankCode(response.getBankCode()).orElseThrow(
                () -> new CustomException(ErrorCode.WRONG_BANKCODE)
        );
        Account account = Account.builder().bank(bank).member(membership.getMember()).maskedAccountNumber(maskedAccountNumber(response.getAccountNumber()))
                .accountNumber(AESUtil.encrypt(response.getAccountNumber())).balance(response.getBalance()).
                accountType(response.getAccountType()).fintecNumber(response.getFintechUseNum()).productName(response.getProductName()).build();
        Account savedAccount = accountRepository.save(account);
        return AccountIssuedResponse.from(savedAccount);
    }

    @Transactional
    public AccountLinkResponse accountLink(Long agitId, AccountLinkRequest accountLinkRequest) {
        Member member = memberRepository.findById(accountLinkRequest.getMemberId()).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        Membership membership = memberShipRepository.findByAgitAndRole(agit, MemberRole.LEADER).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_ACCOUNT_NOT_FOUND)
        );
        String ci = membership.getMember().getCi();
        if (!member.getCi().equals(ci)) {
            throw new CustomException(ErrorCode.AUTHORIZED_ACCOUNT_CREATION);
        }
        Account account = accountRepository.findByFintecNumber(accountLinkRequest.getFintechUseNum()).orElseThrow(
                () -> new CustomException(ErrorCode.ACCOUNT_NOT_MATCHED_FINNUM)
        );
        Optional<AgitAndAccount> optionalAgitAndAccount = agitAndAccountRepository.findByAgitAndAccount(agit, account);
        if(optionalAgitAndAccount.isPresent())
            throw new CustomException(ErrorCode.PRESENT_AGIT_AND_ACCOUNT);
        AgitAndAccount agitAndAccount = AgitAndAccount.builder().account(account).agit(agit).build();
        log.info("{}번의 아지트({})와 모임통장({})이 연결되었습니다.", agitId, agit.getAgitName(), ci);
        AgitAndAccount savedAgitAndAccount = agitAndAccountRepository.save(agitAndAccount);
        agit.setAgitAndAccount(savedAgitAndAccount);
        return AccountLinkResponse
                        .builder()
                        .agitId(agitId)
                        .accountId(savedAgitAndAccount.getId())
                        .accountNumber(AESUtil.decrypt(savedAgitAndAccount.getAccount().getAccountNumber()))
                        .build();
    }

    public AccountInfoResponse getAllAccounts(Long agitId, MemberIdRequest memberIdRequest) {
        Member member = memberRepository.findById(memberIdRequest.getMemberId()).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_MATCHED_MEMBER)
        );
        String ci = membership.getMember().getCi();
        if (!member.getCi().equals(ci)) {
            throw new CustomException(ErrorCode.AUTHORIZED_ACCOUNT_CREATION);
        }
        CIOnlyRequest request = CIOnlyRequest.builder().ci(ci).build();
        AccountInfoResponse allAccounts = coreService.getAllAccounts(request);
        for(AccountIssuedResponse response : allAccounts.getAccountList()){
            Optional<Account> findAccount = accountRepository.findByAccountNumber(AESUtil.encrypt(response.getAccountNumber()));
            if(findAccount.isEmpty()) continue;
            findAccount.get().setBalance(response.getBalance());
        }
        return allAccounts;
    }

    private String maskedAccountNumber(String accountNumber) {
        String maskingResult = "";

        if (accountNumber.length() >= 7) {
            maskingResult = accountNumber.replaceAll("(?<=.{4}).(?=.{2})", "*");
        } else {
            maskingResult = accountNumber;
        }

        return maskingResult;

    }
    public TransactionDetailResponse accountDetails(Long agitId, Long memberId, AccountDetailsRequest accountDetailsResponse) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND)
        );
        String ci = membership.getMember().getCi();
        if (!member.getCi().equals(ci)) {
            throw new CustomException(ErrorCode.AUTHORIZED_ACCOUNT_CREATION);
        }
        String fintecNumber = agit.getAgitAndAccount().getAccount().getFintecNumber();
        accountRepository.findByFintecNumber(fintecNumber).orElseThrow(
                () -> new CustomException(ErrorCode.ACCOUNT_NOT_MATCHED_FINNUM)
        );
        TransactionDetailRequest transactionDetailRequest = TransactionDetailRequest
                .builder()
                .ci(member.getCi())
                .fintechUseNum(fintecNumber)
                .transactionType(accountDetailsResponse.getTransactionType())
                .selectPeriod(accountDetailsResponse.getSelectPeriod())
                .order(accountDetailsResponse.getOrder())
                .build();
        return coreService.filteredAccountHistory(transactionDetailRequest);
    }

    public TransactionDetailResponse getAccountDeposit(Long agitId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND)
        );
        String ci = membership.getMember().getCi();
        if (!member.getCi().equals(ci)) {
            throw new CustomException(ErrorCode.AUTHORIZED_ACCOUNT_CREATION);
        }
        String fintecNumber = agit.getAgitAndAccount().getAccount().getFintecNumber();
        accountRepository.findByFintecNumber(fintecNumber).orElseThrow(
                () -> new CustomException(ErrorCode.ACCOUNT_NOT_MATCHED_FINNUM)
        );
        TransactionDetailRequest transactionDetailRequest = TransactionDetailRequest.builder().ci(ci).fintechUseNum(fintecNumber).selectPeriod(1).transactionType(TranType.DEPOSIT.toString()).order("ASC").build();
        TransactionDetailResponse transactionDetailResponse = coreService.filteredAccountHistory(transactionDetailRequest);
        List<TransactionHistoryResponse> filteredTranList = transactionDetailResponse.getTranList().stream().filter(response ->
                response.getTransactionTime().getMonth().equals(LocalDateTime.now().getMonth()) && (response.getTransactionTime().getYear() == LocalDateTime.now().getYear())).toList();
        transactionDetailResponse.setTranList(filteredTranList);
        return transactionDetailResponse;
    }
}
