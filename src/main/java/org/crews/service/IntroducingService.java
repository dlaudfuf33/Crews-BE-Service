package org.crews.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.IntroducingRequest;
import org.crews.dto.response.IntroducingResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.Agit;
import org.crews.model.Introducing;
import org.crews.model.Member;
import org.crews.model.Membership;
import org.crews.model.constants.MemberRole;
import org.crews.repository.AgitRepository;
import org.crews.repository.IntroducingRepository;
import org.crews.repository.MemberRepository;
import org.crews.repository.MemberShipRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class IntroducingService {
    private final IntroducingRepository introducingRepository;
    private final AgitRepository agitRepository;
    private final MemberRepository memberRepository;
    private final MemberShipRepository memberShipRepository;

    public IntroducingResponse getIntroducing(Long memberId, Long agitId) {
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));
        System.out.println("memberId = " + memberId);
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
    public IntroducingResponse patchIntroducing(Long memberId, Long agitId, IntroducingRequest introducingRequest) {
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));

        if(!membership.getRole().equals(MemberRole.LEADER)){
            throw new CustomException(ErrorCode.AUTHORIZED_MEETING_CREATION);
        }

        return  null;
    }
}
