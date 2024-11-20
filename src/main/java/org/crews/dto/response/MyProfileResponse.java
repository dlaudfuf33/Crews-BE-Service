package org.crews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crews.model.Member;
import org.crews.model.MemberAndInteresting;

import java.util.List;

@Getter
@Builder
public class MyProfileResponse {
    private String profileImage;
    private String email;
    private String name;
    private String nickname;
    private List<InterestingResponse> interests;

    public static MyProfileResponse from(Member member) {
        return MyProfileResponse.builder()
                .profileImage(member.getProfileImage())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickName())
                .interests(
                        member.getMemberAndInterestings().stream()
                                .map(MyProfileResponse::toInterestingResponse)
                                .toList()
                )
                .build();
    }
    private static InterestingResponse toInterestingResponse(MemberAndInteresting memberInteresting) {
        return InterestingResponse.from(memberInteresting.getInteresting());
    }
}
