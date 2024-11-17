package org.crews.service;

import static org.crews.excaption.ErrorCode.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.response.AgitResponse;
import org.crews.dto.AgitRequest;
import org.crews.excaption.CustomException;
import org.crews.excaption.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.MemberRole;
import org.crews.repository.AgitRepository;
import org.crews.repository.InterestingAndAgitRepository;
import org.crews.repository.InterestingRepository;
import org.crews.repository.IntroducingRepository;
import org.crews.repository.MemberRepository;
import org.crews.repository.MemberShipRepository;
import org.crews.repository.SubjectRepository;
import org.springframework.stereotype.Service;

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
    private final MemberShipRepository memberShipRepository;
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;
    public List<AgitResponse> getAllAgits(){
        return agitRepository.findAllWithFetchJoin().stream().map(AgitResponse::FROM).toList();
    }
    @Transactional
    public boolean generateAgit(AgitRequest agitRequest) {
        try {

            Subject subject = subjectRepository.findById(agitRequest.getSubject()).orElseThrow(
                () -> new CustomException(SUBJECT_NOT_FOUND)
            );
            Agit agit = Agit.builder().agitName(agitRequest.getName()).isDue(false)
                .maxPerson(30).currentPerson(1).isDeleted(false).subject(subject).introduction(agitRequest.getIntroduction())
                .build();

            Agit savedAgit = agitRepository.save(agit);

            List<InterestingAndAgit> interestingAndAgits = new ArrayList<>();
            for (Long interest : agitRequest.getInterests()) {
                Interesting interesting = interestingRepository.findById(interest)
                        .orElseThrow(() -> new CustomException(ErrorCode.INTERESTS_NOT_FOUND));
                InterestingAndAgit interestingAndAgit = new InterestingAndAgit();
                interestingAndAgit.setAgit(savedAgit);
                interestingAndAgit.setInteresting(interesting);
                interestingAndAgits.add(interestingAndAgit);
            }
            interestingAndAgitRepository.saveAll(interestingAndAgits);
            Member member = memberRepository.findById(agitRequest.getMemberId()).orElseThrow(
                () -> new CustomException(MEMBER_NOT_FOUND)
            );
            Membership membership = Membership.builder().agit(savedAgit).member(member).role(MemberRole.LEADER).joinedAt(
                LocalDateTime.now()).build();
            memberShipRepository.save(membership);

            return true;
        } catch (Exception e) {
            log.error("Error generating Agit: ", e);
            return false;
        }
    }
}
