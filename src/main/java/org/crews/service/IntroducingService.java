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

        if(introducingRequest.getDeleteInterests() != null && !introducingRequest.getDeleteInterests().isEmpty()){
            List<Long> deleteInterestId = introducingRequest.getDeleteInterests();
            List<Interesting> interestsToDelete = interestingRepository.findByIdIn(deleteInterestId);

            for (Long interestId : deleteInterestId) {
                if (!interestingRepository.existsById(interestId)) throw new CustomException(ErrorCode.INVALID_INTEREST_ID);
            }

            interestingAndAgitRepository.deleteByAgitAndInterestingIn(checkedResult.getAgit(), interestsToDelete);
        }

        if(introducingRequest.getAddInterests() != null && !introducingRequest.getAddInterests().isEmpty()){
            List<Long> addInterestId = introducingRequest.getAddInterests();
            List<Interesting> interestsToAdd = interestingRepository.findByIdIn(addInterestId);

            for (Long interestId : addInterestId) {
                if (!interestingRepository.existsById(interestId)) throw new CustomException(ErrorCode.INVALID_INTEREST_ID);
            }

            List<Long> existingInterests = interestingAndAgitRepository.findByAgit(checkedResult.getAgit()).stream()
                    .map(interestingAndAgit -> interestingAndAgit.getInteresting().getId())
                    .collect(Collectors.toList());

            List<Interesting> filteredInterests = interestsToAdd.stream()
                    .filter(interest -> !existingInterests.contains(interest.getId()))
                    .collect(Collectors.toList());

            List<InterestingAndAgit> newInterests = filteredInterests.stream()
                    .map(interest -> InterestingAndAgit.builder()
                            .agit(checkedResult.getAgit())
                            .interesting(interest)
                            .build())
                    .collect(Collectors.toList());

            interestingAndAgitRepository.saveAll(newInterests);
        }

        introducing.setImage(introducingRequest.getImage());
        introducing.setIntroduce(introducingRequest.getIntroduce());
        introducing.setContent(introducingRequest.getContent());

        return IntroducingResponse.of("LEADER",introducing);
    }
}
