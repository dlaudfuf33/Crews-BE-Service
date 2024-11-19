package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.*;
import org.crews.dto.request.AccountLinkRequest;
import org.crews.dto.request.MemberIdDto;
import org.crews.dto.core.AccountIssuedResponse;
import org.crews.dto.core.AccountOneResponse;
import org.crews.dto.core.CommonRequest;

import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.MemberRole;
import org.crews.repository.*;
import org.crews.utils.AESUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
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
        return coreService.accountInfo(commonRequest);
    }

    @Transactional
    public AccountIssuedResponse accountIssued(Long agitId, MemberIdDto memberIdDto, MemberRole memberRole) {
        Member member = memberRepository.findById(memberIdDto.getMemberId()).orElseThrow(
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
                accountType(response.getAccountType()).fintecNumber(response.getFintechUseNum()).build();
        Account savedAccount = accountRepository.save(account);
        return AccountIssuedResponse.from(savedAccount);
    }

    @Transactional
    public AgitAndAccount accountLink(Long agitId, AccountLinkRequest accountLinkRequest) {
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
        AgitAndAccount agitAndAccount = AgitAndAccount.builder().account(account).agit(agit).build();
        log.info("{}번의 아지트({})와 모임통장({})이 연결되었습니다.", agitId, agit.getAgitName(), ci);
        AgitAndAccount savedAgitAndAccount = agitAndAccountRepository.save(agitAndAccount);
        agit.setAgitAndAccount(savedAgitAndAccount);
        return savedAgitAndAccount;
    }

    public AccountInfoResponse accountDetails(Long agitId, Long accountId, AccountLinkRequest accountLinkRequest) {
        Member member = memberRepository.findById(accountLinkRequest.getMemberId()).orElseThrow(
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
        accountRepository.findByIdAndFintecNumber(accountId,accountLinkRequest.getFintechUseNum()).orElseThrow(
                () -> new CustomException(ErrorCode.ACCOUNT_NOT_MATCHED_FINNUM)
        );
        CIOnlyRequest request = CIOnlyRequest.builder().ci(ci).build();
        return coreService.accountDetails(request);
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
}
