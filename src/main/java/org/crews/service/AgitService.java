package org.crews.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.AgitRequest;
import org.crews.dto.AgitResponse;
import org.crews.excaption.CustomException;
import org.crews.excaption.ErrorCode;
import org.crews.model.*;
import org.crews.repository.AgitRepository;
import org.crews.repository.InterestingAndAgitRepository;
import org.crews.repository.InterestingRepository;
import org.crews.repository.IntroducingRepository;
import org.springframework.stereotype.Service;

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
    public List<AgitResponse> getAllAgits(){
        return agitRepository.findAllWithFetchJoin().stream().map(AgitResponse::FROM).toList();
    }
    @Transactional
    public boolean generateAgit(AgitRequest agitRequest) {
        try {
            Agit agit = new Agit();

            agit.setAgitName(agitRequest.getName());

            Subject subject = new Subject();
            subject.setId(agitRequest.getSubject());
            agit.setSubject(subject);

            Agit savedAgit = agitRepository.save(agit);

            Introducing introducing = new Introducing();
            introducing.setImage(agitRequest.getImage());
            introducing.setIntroduce(agitRequest.getIntroduction());
            introducing.setContent(agitRequest.getFeature());
            introducing.setAgit(savedAgit);
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
            interestingAndAgitRepository.saveAll(interestingAndAgits);

            return true;
        } catch (Exception e) {
            log.error("Error generating Agit: ", e);
            return false;
        }
    }
}
