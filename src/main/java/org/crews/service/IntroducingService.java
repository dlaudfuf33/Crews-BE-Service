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
import org.crews.model.constants.MemberRole;
import org.crews.repository.*;
import org.crews.utils.CheckExceptionUtil;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public IntroducingResponse getIntroducing(Long memberId, Long agitId) {
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));

        String memberRole;
        if (memberId == 0L) {
            memberRole = "NOTMEMBER";
        }else{
            Member member = memberRepository.findById(memberId).orElseThrow(
                    ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            Optional<Membership> membershipOptional = Optional.ofNullable(
                    memberShipRepository.findByMemberAndAgit(member, agit).orElse(null));
            memberRole = membershipOptional
                    .map(membership -> membership.getRole().toString())
                    .orElse("NOTMEMBER");
        }
        Introducing introducing = agit.getIntroducing();

        return IntroducingResponse.of(memberRole,introducing);
    }

    @Transactional
    public IntroducingResponse updateIntroducing(Long memberId, Long agitId, IntroducingRequest introducingRequest) {
        AgitVaildationResponse checkedResult = checkExceptionUtil.checkAgitException(memberId, agitId);

        if(!checkedResult.getMembership().getRole().equals(MemberRole.LEADER)){
            throw new CustomException(ErrorCode.AUTHORIZED_INTRODUCING_UPDATE);
        }

        Introducing introducing = checkedResult.getAgit().getIntroducing();
        interestingAndAgitRepository.deleteByAgit(checkedResult.getAgit());
        interestingAndAgitRepository.flush();

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
                    .collect(Collectors.toList());

            interestingAndAgitRepository.saveAll(updateInterestingAndAgit);
            if (updateInterestingAndAgit.size() < 1 || updateInterestingAndAgit.size() > 3) throw new CustomException(ErrorCode.INVALID_INTERESTS_COUNT);
        }else{
            throw new CustomException(ErrorCode.INVALID_INTERESTS_COUNT);
        }

        introducing.setImage(introducingRequest.getImage());
        introducing.setIntroduce(introducingRequest.getIntroduce());
        introducing.setContent(introducingRequest.getContent());

        return IntroducingResponse.of("LEADER",introducing);
    }
}
