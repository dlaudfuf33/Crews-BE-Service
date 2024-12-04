package org.crews.utils;

import lombok.RequiredArgsConstructor;
import org.crews.dto.response.AgitVaildationResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.Agit;
import org.crews.model.Feed;
import org.crews.model.Member;
import org.crews.model.Membership;
import org.crews.model.constants.AgitRole;
import org.crews.repository.AgitRepository;
import org.crews.repository.FeedRepository;
import org.crews.repository.MemberRepository;
import org.crews.repository.MembershipRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckExceptionUtil {

    private final AgitRepository agitRepository;
    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;
    private final FeedRepository feedRepository;

    public AgitVaildationResponse checkAgitException(Long memberId, Long agitId) {
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Membership membership = membershipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));

        return new AgitVaildationResponse(agit, member, membership);
    }

    public AgitVaildationResponse checkFeedException(Long memberId,Long agitId, Long feedId) {

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new CustomException(ErrorCode.FEED_NOT_FOUND));
        Agit agit = feed.getAgit();
        if(agit.getId()!=agitId){
            throw new CustomException(ErrorCode.AGIT_NOT_FOUND);
        }
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Membership membership = membershipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));

        return new AgitVaildationResponse(agit, member, membership, feed);
    }

    public Membership validateLeader(Long memberId, Long agitId, AgitRole requiredRole) {
        Membership membership = membershipRepository.findByMemberIdAndAgitId(memberId, agitId).orElseThrow(()->new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));
        AgitRole agitRole = membership.getAgitRole();
        if(!agitRole.equals(requiredRole)){
            throw new CustomException(ErrorCode.AUTHORIZED_CAPTAIN_ONLY);
        }
        return membership;
    }

    public Membership validateRole(Long memberId, Long agitId, AgitRole requiredRole){
        Membership membership = membershipRepository.findByMemberIdAndAgitId(memberId, agitId).orElseThrow(()->new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));
        AgitRole agitRole = membership.getAgitRole();
        if(!agitRole.equals(requiredRole)){
            throw new CustomException(ErrorCode.NOT_MATCHED_ROLE);
        }
        return membership;
    }
}
