package org.crews.service;

import static org.crews.exception.ErrorCode.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.response.AgitResponse;
import org.crews.dto.request.AgitRequest;
import org.crews.dto.response.DuesAlarmResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.MemberRole;
import org.crews.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class AgitService {
    private final AgitRepository agitRepository;
    private final InterestingRepository interestingRepository;
    private final InterestingAndAgitRepository interestingAndAgitRepository;
    private final IntroducingRepository introducingRepository;
    private final MemberShipRepository memberShipRepository;
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;
    private final DuesRepository duesRepository;
    private final CommonDuesRepository commonDuesRepository;
    public List<AgitResponse> getAllAgits(){
        return agitRepository.findAllWithFetchJoin().stream().map(AgitResponse::from).toList();
    }
    @Transactional
    public AgitResponse generateAgit(AgitRequest agitRequest) {
        Subject subject = subjectRepository.findById(agitRequest.getSubject()).orElseThrow(
                () -> new CustomException(SUBJECT_NOT_FOUND)
        );
        Agit agit = Agit.builder().agitName(agitRequest.getName()).isDue(false)
                .maxPerson(30).currentPerson(1).isDeleted(false).subject(subject).introduction(agitRequest.getIntroduction())
                .build();

        Agit savedAgit = agitRepository.save(agit);
        Introducing introducing = Introducing.builder().agit(savedAgit).introduce(savedAgit.getIntroduction()).image("").content("").build();
        introducingRepository.save(introducing);
        List<InterestingAndAgit> interestingAndAgits = new ArrayList<>();
        for (Long interest : agitRequest.getInterests()) {
            Interesting interesting = interestingRepository.findById(interest)
                    .orElseThrow(() -> new CustomException(ErrorCode.INTERESTS_NOT_FOUND));
            InterestingAndAgit interestingAndAgit = new InterestingAndAgit();
            interestingAndAgit.setAgit(savedAgit);
            interestingAndAgit.setInteresting(interesting);
            interestingAndAgits.add(interestingAndAgit);
        }
        savedAgit.getInterestingAndAgits().addAll(interestingAndAgits);
        interestingAndAgitRepository.saveAllAndFlush(interestingAndAgits);
        Member member = memberRepository.findById(agitRequest.getMemberId()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
        );
        Membership membership = Membership.builder().agit(savedAgit).member(member).role(MemberRole.LEADER).joinedAt(
                LocalDateTime.now()).build();
        memberShipRepository.save(membership);
        return AgitResponse.from(savedAgit);

    }

    public DuesAlarmResponse getDuesAlarm(Long agitId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new CustomException(MEMBERSHIP_NOT_FOUND)
        );
        CommonDues commonDues = commonDuesRepository.findByAgit(agit).orElseThrow(
                () -> new CustomException(COMMON_DUES_NOT_FOUND)
        );
        List<Dues> duesList = duesRepository.findByMembershipAndCommonDues(membership, commonDues)
                .stream().filter(
                        content -> content.getDueDate().getMonth().equals(LocalDate.now().getMonth())
                                && (content.getDueDate().getYear() == LocalDate.now().getYear()))
                .toList();
        if(duesList.isEmpty()){
            return DuesAlarmResponse.builder().duesAmount(commonDues.getDueAmount()).build();
        }else {
            BigDecimal amount = BigDecimal.ZERO;
            for(Dues dues : duesList){
                amount = amount.add(dues.getDueAmount());
            }
            if(amount.compareTo(commonDues.getDueAmount()) < 0){
                return DuesAlarmResponse.builder().duesAmount(commonDues.getDueAmount().subtract(amount)).build();
            }
            else
                return DuesAlarmResponse.builder().build();
        }
    }
}
