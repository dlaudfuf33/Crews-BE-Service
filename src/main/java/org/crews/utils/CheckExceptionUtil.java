package org.crews.utils;

import lombok.RequiredArgsConstructor;
import org.crews.dto.response.AgitValidationResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.Agit;
import org.crews.model.Feed;
import org.crews.model.Member;
import org.crews.model.Membership;
import org.crews.repository.AgitRepository;
import org.crews.repository.FeedRepository;
import org.crews.repository.MemberRepository;
import org.crews.repository.MemberShipRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckExceptionUtil {

    private final AgitRepository agitRepository;
    private final MemberRepository memberRepository;
    private final MemberShipRepository memberShipRepository;
    private final FeedRepository feedRepository;

    public AgitValidationResponse checkAgitException(Long memberId, Long agitId) {
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));

        return new AgitValidationResponse(agit, member, membership);
    }

    public AgitValidationResponse checkFeedException(Long memberId, Long agitId, Long feedId) {

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new CustomException(ErrorCode.FEED_NOT_FOUND));
        Agit agit = feed.getAgit();
        if(!agit.getId().equals(agitId)){
            throw new CustomException(ErrorCode.AGIT_NOT_FOUND);
        }
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));

        return new AgitValidationResponse(agit, member, membership, feed);
    }
}
