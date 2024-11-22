package org.crews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crews.model.Member;
import org.crews.model.MemberAndInteresting;
import org.crews.utils.AESUtil;

import java.util.List;

@Getter
@Builder
public class MyProfileResponse {
    private String profileImage;
    private String email;
    private String name;
    private String nickname;
    private List<InterestResponse> interests;

    public static MyProfileResponse from(Member member) {
        return MyProfileResponse.builder()
                .profileImage(member.getProfileImage())
                .email(AESUtil.decrypt(member.getEmail()))
                .name(AESUtil.decrypt(member.getName()))
                .nickname(member.getNickName())
                .interests(
                        member.getMemberAndInterestings().stream()
                                .map(MyProfileResponse::toInterestingResponse)
                                .toList()
                )
                .build();
    }
    private static InterestResponse toInterestingResponse(MemberAndInteresting memberInteresting) {
        return InterestResponse.from(memberInteresting.getInteresting());
    }
}
