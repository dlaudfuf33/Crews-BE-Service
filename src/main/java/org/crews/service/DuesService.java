package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.crews.dto.core.AccountInfoOfDate;
import org.crews.dto.request.DuesSaveRequest;
import org.crews.dto.response.*;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.AgitRole;
import org.crews.model.constants.TranType;
import org.crews.repository.*;
import org.crews.utils.AESUtil;
import org.crews.utils.DateUtil;
import org.crews.utils.DuesCommon;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DuesService {

    private final MemberRepository memberRepository;
    private final AgitRepository agitRepository;
    private final MembershipRepository memberShipRepository;
    private final CommonDuesRepository commonDuesRepository;
    private final AccountRepository accountRepository;
    private final DuesRepository duesRepository;

    private final CoreService coreService;

    @Transactional
    public GetDuesResponse getDues(Long agitId, Long memberId, Integer year, Integer month) {
        LocalDateTime today = LocalDateTime.now();
        if((today.getMonthValue() < month && today.getYear() == year) || today.getYear() < year)
            throw new CustomException(ErrorCode.DATE_AFTER_NOW);
        Member member = memberRepository.findById(memberId).orElseThrow(
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
        AccountInfoOfDate accountInfoOfDate = AccountInfoOfDate.builder()
            .ci(ci)
            .fintechUseNum(agit.getAgitAndAccount().getAccount().getFintecNumber())
            .tranType(TranType.DEPOSIT)
            .year(year)
            .month(month)
            .build();
        TransactionDetailResponse response = coreService.DateAccountHistory(accountInfoOfDate);
        List<TransactionHistoryResponse> tranList = response.getTranList();
        List<Dues> saveDues = new ArrayList<>();
        CommonDues commonDues = agit.getCommonDues();
        if(commonDues == null)
            throw new CustomException(ErrorCode.COMMON_DUES_NOT_FOUND);
        List<Dues> duesList = duesRepository.findByCommonDues(commonDues);
        for (TransactionHistoryResponse dto : tranList){
            Optional<Account> optionalAccount = accountRepository.findByAccountNumber(AESUtil.encrypt(dto.getCounterpartyAccountNum()));
            if(optionalAccount.isEmpty()) {
                continue;
            }
            Account account = optionalAccount.get();
            Member filterMember = account.getMember();
            Optional<Membership> optionalMembership = memberShipRepository.findByMemberAndAgit(filterMember, agit)
                .filter(ms -> !ms.getCreatedAt()
					.isAfter(LocalDateTime.of(year, month, DateUtil.getLastDayOfMonth(year, month), 23, 59, 59)));
            if(optionalMembership.isEmpty()) {
                continue;
            }
            Optional<Dues> optionalDues = duesList.stream()
                .filter(dues -> dues.getMembership().equals(optionalMembership.get()) && dues.getDueDate().equals(dto.getTransactionTime()))
                    .findAny();
            if (optionalDues.isEmpty()){
                Dues buildDues = Dues.builder().commonDues(commonDues).dueDate(dto.getTransactionTime()).dueAmount(dto.getTranAmount())
                        .membership(optionalMembership.get()).isPaid(false).accountNumber(account.getAccountNumber())
                        .productName(account.getProductName()).agitName(agit.getAgitName()).standardDate(DateUtil.generateStandardDate(year,month,dto.getTransactionTime())).build();
                saveDues.add(buildDues);
            }
        }
        duesRepository.saveAll(saveDues);
        List<Dues> dues = duesRepository.findByCommonDues(agit.getCommonDues()).stream().filter(content ->
            content.getStandardDate().getMonthValue() == month && (content.getStandardDate().getYear() == year))
                .toList();

        List<Membership> searchMembershipList = memberShipRepository.findByAgit(agit);

        List<Member> memberList = new ArrayList<>(searchMembershipList.stream()
            .filter(ms -> !ms.getCreatedAt()
                .isAfter(LocalDateTime.of(year, month, DateUtil.getLastDayOfMonth(year, month), 23, 59, 59)))
            .map(Membership::getMember).toList());
        Map<Member, BigDecimal> memberMap = DuesCommon.calculateTotalDueAmountByMembership(dues);
        memberMap.forEach((filterMember, toTotalAmount) -> {
            if(agit.getCommonDues() == null){
                throw new CustomException(ErrorCode.COMMON_DUES_NOT_FOUND);
            }
            if(toTotalAmount.compareTo(agit.getCommonDues().getDueAmount()) >= 0){
                memberList.remove(filterMember);
                DuesCommon.setPaidChange(dues,filterMember,true);
            }else {
                DuesCommon.setPaidChange(dues,filterMember,false);
            }
        });
        List<ProfileResponse> profileResponses = memberList.stream().map(ProfileResponse::from).toList();

        return GetDuesResponse.builder().profileResponses(profileResponses).memberCount(profileResponses.size()).build();
    }




    @Transactional
    public DuesSaveResponse duesSaveCommon(Long agitId, Long memberId, DuesSaveRequest duesSaveRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(
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
        Optional<CommonDues> optionalCommonDues = commonDuesRepository.findByAgit(agit);
        CommonDues savedCommonDues;
        if(optionalCommonDues.isEmpty()){
            CommonDues buildCommonDues = CommonDues.builder().dueAmount(duesSaveRequest.getDueAmount()).dueDay(duesSaveRequest.getDueDay())
                    .agit(agit).build();
            savedCommonDues = commonDuesRepository.save(buildCommonDues);
            agit.setCommonDues(savedCommonDues);
        }else {
            CommonDues commonDues = optionalCommonDues.get();
            commonDues.setDueAmount(duesSaveRequest.getDueAmount());
            commonDues.setDueDay(duesSaveRequest.getDueDay());
            savedCommonDues = commonDues;
            agit.setCommonDues(commonDues);
        }
        return DuesSaveResponse.builder().dueDay(savedCommonDues.getDueDay()).dueAmount(savedCommonDues.getDueAmount()).build();
    }

    public DuesSaveResponse getDuesCommon(Long agitId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        Membership membership = memberShipRepository.findByMemberAndAgit(member,agit).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_ACCOUNT_NOT_FOUND)
        );
        String ci = membership.getMember().getCi();
        if (!member.getCi().equals(ci)) {
            throw new CustomException(ErrorCode.AUTHORIZED_ACCOUNT_CREATION);
        }
        CommonDues commonDues = commonDuesRepository.findByAgit(agit).orElse(null);
        if(commonDues == null)
            return DuesSaveResponse.builder().dueAmount(null).dueAmount(null)
                    .minYear(membership.getCreatedAt().getYear()).minMonth(membership.getCreatedAt().getMonthValue()).build();
        else
            return DuesSaveResponse.builder().dueDay(commonDues.getDueDay()).dueAmount(commonDues.getDueAmount())
                    .minYear(membership.getCreatedAt().getYear()).minMonth(membership.getCreatedAt().getMonthValue()).build();
    }


}
