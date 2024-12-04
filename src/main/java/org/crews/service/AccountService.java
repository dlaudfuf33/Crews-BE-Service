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
import org.crews.model.constants.AgitRole;
import org.crews.model.constants.TranType;
import org.crews.repository.*;
import org.crews.utils.AESUtil;
import org.crews.utils.DateUtil;
import org.crews.utils.DuesCommon;
import org.crews.utils.MaskedNumber;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    private final DuesRepository duesRepository;
    private final AgitAndAccountRepository agitAndAccountRepository;
    private final CoreService coreService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


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
    public AccountIssuedResponse accountIssued(Long agitId, Long memberId, ProductRequest productRequest, AgitRole memberRole) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        Membership membership = memberShipRepository.findByAgitAndAgitRole(agit, memberRole).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_ACCOUNT_NOT_FOUND)
        );
        String ci = membership.getMember().getCi();
        if (!member.getCi().equals(ci)) {
            throw new CustomException(ErrorCode.AUTHORIZED_ACCOUNT_CREATION);
        }
        Optional<AgitAndAccount> crewAccount = agitAndAccountRepository.findByAgit(agit);
        if(crewAccount.isPresent())
            throw new CustomException(ErrorCode.PRESENT_AGIT_AND_ACCOUNT);
        ProductInfoRequest productInfoRequest = ProductInfoRequest.builder().ci(ci).productId(productRequest.getProductId()).build();
        AccountIssuedResponse response = coreService.accountIssued(productInfoRequest);
        Bank bank = bankRepository.findByBankCode(response.getBankCode()).orElseThrow(
                () -> new CustomException(ErrorCode.WRONG_BANKCODE)
        );
        Account account = Account.builder().bank(bank).member(membership.getMember()).maskedAccountNumber(MaskedNumber.maskedAccountNumber(response.getAccountNumber()))
                .accountNumber(AESUtil.encrypt(response.getAccountNumber())).balance(response.getBalance()).
                accountType(response.getAccountType()).fintecNumber(response.getFintechUseNum()).productName(response.getProductName()).build();
        Account savedAccount = accountRepository.save(account);
        Optional<AgitAndAccount> optionalAgitAndAccount = agitAndAccountRepository.findByAgitAndAccount(agit, savedAccount);
        if (optionalAgitAndAccount.isPresent())
            throw new CustomException(ErrorCode.PRESENT_AGIT_AND_ACCOUNT);
        AgitAndAccount agitAndAccount = AgitAndAccount.builder().account(account).agit(agit).build();
        log.info("{}번의 아지트({})와 모임통장({})이 연결되었습니다.", agitId, agit.getAgitName(), ci);
        agitAndAccountRepository.save(agitAndAccount);
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
        Membership membership = memberShipRepository.findByAgitAndAgitRole(agit, AgitRole.LEADER).orElseThrow(
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
        if (optionalAgitAndAccount.isPresent())
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

    public AccountInfoResponse getAllAccounts(Long agitId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
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
        for (AccountIssuedResponse response : allAccounts.getAccountList()) {
            Optional<Account> findAccount = accountRepository.findByAccountNumber(AESUtil.encrypt(response.getAccountNumber()));
            if (findAccount.isEmpty()) continue;
            findAccount.get().setBalance(response.getBalance());
        }
        return allAccounts;
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

    public TransactionDetailResponse getAccountDeposit(Long agitId, Long memberId, Integer year, Integer month) {
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
        TransactionDetailRequest transactionDetailRequest = TransactionDetailRequest.builder().ci(ci).fintechUseNum(fintecNumber).selectPeriod(1).transactionType(TranType.DEPOSIT.toString()).order("DESC").build();
        TransactionDetailResponse transactionDetailResponse = coreService.filteredAccountHistory(transactionDetailRequest);
        List<TransactionHistoryResponse> filteredTranList = transactionDetailResponse.getTranList().stream().filter(response ->
                response.getTransactionTime().getMonthValue() == month && (response.getTransactionTime().getYear() == year)).toList();
        transactionDetailResponse.setTranList(filteredTranList);
        return transactionDetailResponse;
    }

    @Transactional
    public ApiResponse<TransferResponse> transferCrewAccount(Long agitId, Long memberId, AccountTransferRequest accountTransferRequest) {
        Integer year = accountTransferRequest.getYear();
        Integer month = accountTransferRequest.getMonth();
        if(year == null) year = LocalDateTime.now().getYear();
        if(month == null) month = LocalDateTime.now().getMonthValue();
        final Integer finalMonth = month;
        final Integer finalYear = year;
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.PINNUMBER_AND_ID_NOT_MATCH)
        );
        if(!bCryptPasswordEncoder.matches(accountTransferRequest.getPinNumber(), member.getPinNumber())){
            throw new CustomException(ErrorCode.VERIFY_PIN_MISMATCH);
        }
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
        Account account = accountRepository.findById(accountTransferRequest.getAccountId()).orElseThrow(
                () -> new CustomException(ErrorCode.ACCOUNT_ID_NOT_FOUND)
        );
        Account recvAccount = accountRepository.findByAccountNumber(AESUtil.encrypt(accountTransferRequest.getRecvAccountNumber())).orElseThrow(
                () -> new CustomException(ErrorCode.ACCOUNT_ID_NOT_FOUND)
        );
        String fintechNum = account.getFintecNumber();
        Optional<Membership> optionalMembership = memberShipRepository.findByMemberAndAgit(member, agit).filter(ms -> !ms.getCreatedAt()
                .isAfter(LocalDateTime.of(finalYear, finalMonth, DateUtil.getLastDayOfMonth(finalYear, finalMonth), 23, 59, 59)));
        if (optionalMembership.isEmpty()) {
            throw new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND);
        }
        TransferRequest transferRequest = TransferRequest.builder().finUseNum(fintechNum).recvAccountNum(accountTransferRequest.getRecvAccountNumber())
                .amt(accountTransferRequest.getAmount()).description(AESUtil.decrypt(member.getName())).build();
        ApiResponse<TransferResponse> transfer = coreService.transfer(transferRequest);
        BalanceInfoRequest balanceInfoRequest = BalanceInfoRequest.builder().fintecUseNum(account.getFintecNumber()).recvFintecUseNum(recvAccount.getFintecNumber()).build();
        BalanceInfoResponse response = coreService.getBalanceInfo(balanceInfoRequest);
        account.setBalance(response.getAfterBalanceAmt());
        recvAccount.setBalance(response.getRecvAfterBalanceAmt());
        if (recvAccount.getAgitAndAccount() == null) {
            return transfer;
        }
        CommonDues commonDues = agit.getCommonDues();
        if (commonDues == null) return transfer;


        if (transfer.getData() == null) return transfer;
        TransferResponse transferResponse = transfer.getData();

        Dues buildDues = Dues.builder().commonDues(commonDues).dueDate(transferResponse.getTransactionTime()).dueAmount(transferResponse.getAmount())
                .membership(optionalMembership.get()).isPayed(false).accountNumber(account.getAccountNumber())
                .productName(account.getProductName()).agitName(agit.getAgitName())
                .standardDate(DateUtil.generateStandardDate(finalYear,finalMonth,transferResponse.getTransactionTime())).build();
        duesRepository.save(buildDues);


        List<Dues> dues = duesRepository.findByCommonDues(agit.getCommonDues()).stream().filter(content ->
                content.getStandardDate().getMonthValue() == finalMonth && (content.getStandardDate().getYear() == finalYear))
                .toList();

        Map<Member, BigDecimal> memberMap = DuesCommon.calculateTotalDueAmountByMembership(dues);
        memberMap.forEach((filterMember, toTotalAmount) -> {
            if (toTotalAmount.compareTo(agit.getCommonDues().getDueAmount()) >= 0) {
                DuesCommon.setPayedChange(dues, filterMember, true);
            } else {
                DuesCommon.setPayedChange(dues, filterMember, false);
            }
        });

        return transfer;
    }

    @Transactional
    public String accountPermission(Long agitId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND)
        );
        if(!membership.getAgitRole().equals(AgitRole.MEMBER)){
            throw new CustomException(ErrorCode.PERMISSION_NOT_ALLOWED);
        }
        membership.setAgitRole(AgitRole.ADVANCED);
        return "모임통장 권한이 요청되었습니다.";
    }
}
