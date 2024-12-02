package org.crews.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.IntroducingRequest;
import org.crews.dto.response.AgitVaildationResponse;
import org.crews.dto.response.IntroducingResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.AgitRole;
import org.crews.repository.*;
import org.crews.utils.CheckExceptionUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class IntroducingService {
    private final IntroducingRepository introducingRepository;
    private final AgitRepository agitRepository;
    private final MemberRepository memberRepository;
    private final MemberShipRepository memberShipRepository;
    private final CheckExceptionUtil checkExceptionUtil;
    private final InterestingRepository interestingRepository;
    private final InterestingAndAgitRepository interestingAndAgitRepository;

    public IntroducingResponse getIntroducing(Long agitId) {
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));
        Introducing introducing = agit.getIntroducing();

        return IntroducingResponse.of(introducing);
    }

    @Transactional
    public IntroducingResponse updateIntroducing(Long memberId, Long agitId, IntroducingRequest introducingRequest) {
        AgitVaildationResponse checkedResult = checkExceptionUtil.checkAgitException(memberId, agitId);

        if(!checkedResult.getMembership().getAgitRole().equals(AgitRole.LEADER)){
            throw new CustomException(ErrorCode.AUTHORIZED_INTRODUCING_UPDATE);
        }

        Introducing introducing = checkedResult.getAgit().getIntroducing();
        interestingAndAgitRepository.deleteByAgitIdCustom(agitId);

        List<Long> updateInterestsId = introducingRequest.getInterests();
        if(updateInterestsId != null && !updateInterestsId.isEmpty()){
            List<Interesting> updateInteresting = interestingRepository.findByIdIn(updateInterestsId);

            for(Long interestId : updateInterestsId) {
                if(!interestingRepository.existsById(interestId)) throw new CustomException(ErrorCode.INVALID_INTEREST_ID);
            }

            List<InterestingAndAgit> updateInterestingAndAgit = updateInteresting.stream()
                    .map(interest -> InterestingAndAgit.builder()
                            .agit(checkedResult.getAgit())
                            .interesting(interest)
                            .build())
                    .toList();

            if (updateInterestingAndAgit.isEmpty() || updateInterestingAndAgit.size() > 3) throw new CustomException(ErrorCode.INVALID_INTERESTS_COUNT);
            interestingAndAgitRepository.saveAll(updateInterestingAndAgit);
        }else{
            throw new CustomException(ErrorCode.INVALID_INTERESTS_COUNT);
        }

        introducing.setImage(introducingRequest.getImage());
        introducing.setIntroduce(introducingRequest.getIntroduce());
        introducing.setContent(introducingRequest.getContent());

        return IntroducingResponse.of(introducing);
    }
}
