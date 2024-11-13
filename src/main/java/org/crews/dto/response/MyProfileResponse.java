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
    private List<InterestingResponseDto> interests;

    public static MyProfileResponse of(Member member) {
        return MyProfileResponse.builder()
                .profileImage(member.getProfileImage())
                .email(member.getEmail())
                .name(member.getName())
                .nickname(member.getNickName())
                .interests(
                        member.getMemberAndInterestings().stream()
                                .map(MyProfileResponse::convertToInterestingDto)
                                .toList()
                )
                .build();
    }
    private static InterestingResponseDto convertToInterestingDto(MemberAndInteresting memberInteresting) {
        return InterestingResponseDto.of(memberInteresting.getInteresting());
    }
}
