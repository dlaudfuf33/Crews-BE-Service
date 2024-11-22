package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.DuesSaveRequest;
import org.crews.dto.request.TransactionDetailRequest;
import org.crews.dto.response.*;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.MemberRole;
import org.crews.model.constants.TranType;
import org.crews.repository.*;
import org.crews.utils.AESUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DuesService {

    private final MemberRepository memberRepository;
    private final AgitRepository agitRepository;
    private final MemberShipRepository memberShipRepository;
    private final CommonDuesRepository commonDuesRepository;
    private final AccountRepository accountRepository;
    private final DuesRepository duesRepository;

    private final CoreService coreService;

    @Transactional
    public GetDuesResponse getDues(Long agitId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
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
        TransactionDetailRequest transactionDetailRequest = TransactionDetailRequest.builder()
                .ci(member.getCi())
                .selectPeriod(1)
                .fintechUseNum(agit.getAgitAndAccount().getAccount().getFintecNumber())
                .transactionType(TranType.DEPOSIT.toString())
                .order("ASC")
                .build();
        TransactionDetailResponse response = coreService.filteredAccountHistory(transactionDetailRequest);
        List<TransactionHistoryResponse> tranList = response.getTranList();
        List<TransactionHistoryResponse> filteredList = tranList.stream().filter(list ->
                list.getTransactionTime().getMonth().equals(LocalDateTime.now().getMonth())
        ).toList();
        List<Dues> saveDues = new ArrayList<>();
        CommonDues commonDues = agit.getCommonDues();
        List<Dues> duesList = duesRepository.findByCommonDues(commonDues);
        for (TransactionHistoryResponse dto : filteredList){
            Optional<Account> optionalAccount = accountRepository.findByAccountNumber(AESUtil.encrypt(dto.getCounterpartyAccountNum()));
            if(optionalAccount.isEmpty()) {
                continue;
            }
            Account account = optionalAccount.get();
            Member filterMember = account.getMember();
            Optional<Membership> optionalMembership = memberShipRepository.findByMemberAndAgit(filterMember, agit);
            if(optionalMembership.isEmpty()) {
                continue;
            }
            Optional<Dues> optionalDues = duesList.stream().filter(dues -> dues.getMembership().equals(optionalMembership.get()))
                    .filter(dues -> dues.getDueDate().equals(dto.getTransactionTime()))
                    .findAny();
            if (optionalDues.isEmpty()){
                Dues buildDues = Dues.builder().commonDues(commonDues).dueDate(dto.getTransactionTime()).dueAmount(dto.getTranAmount())
                        .membership(optionalMembership.get()).isPayed(false).accountNumber(account.getAccountNumber())
                        .productName(account.getProductName()).agitName(agit.getAgitName()).build();
                saveDues.add(buildDues);
            }
        }
        duesRepository.saveAll(saveDues);
        List<Dues> dues = duesRepository.findByCommonDues(agit.getCommonDues()).stream().filter(content ->
            content.getDueDate().getMonth().equals(LocalDate.now().getMonth()) && (content.getDueDate().getYear() == LocalDate.now().getYear()))
                .toList();

        List<Membership> searchMembershipList = memberShipRepository.findByAgit(agit);
        List<Member> memberList = new ArrayList<>(searchMembershipList.stream().map(Membership::getMember).toList());
        Map<Member, BigDecimal> memberMap = calculateTotalDueAmountByMembership(dues);
        memberMap.forEach((filterMember, toTotalAmount) -> {
            if(toTotalAmount.compareTo(agit.getCommonDues().getDueAmount()) >= 0){
                memberList.remove(filterMember);
                setPayedChange(dues,filterMember,true);
            }else {
                setPayedChange(dues,filterMember,false);
            }
        });
        List<ProfileResponse> profileResponses = memberList.stream().map(ProfileResponse::from).toList();

        return GetDuesResponse.builder().profileResponses(profileResponses).memberCount(profileResponses.size()).build();
    }

    private Map<Member, BigDecimal> calculateTotalDueAmountByMembership(List<Dues> duesList) {
        return duesList.stream()
                .filter(d -> d.getMembership() != null) // Membership이 있는 항목만 처리
                .collect(Collectors.groupingBy(
                        d -> d.getMembership().getMember(), // Membership ID로 그룹화
                        Collectors.mapping(
                                Dues::getDueAmount, // dueAmount를 추출
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add) // 합산
                        )
                ));
    }

    private void setPayedChange(List<Dues> dues, Member filterMember, boolean setPayed) {
        dues.forEach(content -> {
            if (content.getMembership().getMember().equals(filterMember)) {
                content.setPayed(setPayed);
            }
        });
    }


    @Transactional
    public DuesSaveResponse duesSaveCommon(Long agitId, DuesSaveRequest duesSaveRequest) {
        Member member = memberRepository.findById(duesSaveRequest.getMemberId()).orElseThrow(
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
            return DuesSaveResponse.builder().build();
        else
            return DuesSaveResponse.builder().dueDay(commonDues.getDueDay()).dueAmount(commonDues.getDueAmount()).build();
    }
}
