package org.crews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crews.model.Address;
import org.crews.model.Member;
import org.crews.model.MemberAndInteresting;

import java.util.List;

@Getter
@Builder
public class MyinfoResponse {
    private String email;
    private String nickname;
    private List<InterestingResponse> interests;
    private List<Address> address;


    public static MyinfoResponse from(Member member) {
        return MyinfoResponse.builder()
                .email(member.getEmail())
                .nickname(member.getNickName())
                .interests(
                        member.getMemberAndInterestings().stream()
                                .map(MyinfoResponse::convertToInterestingDto)
                                .toList()
                )
                .address(member.getAddresses().stream().toList())
                .build();
    }

    private static InterestingResponse convertToInterestingDto(MemberAndInteresting memberInteresting) {
        return InterestingResponse.from(memberInteresting.getInteresting());
    }
}
