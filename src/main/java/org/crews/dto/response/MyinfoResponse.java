package org.crews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crews.model.Address;
import org.crews.model.Member;
import org.crews.model.MemberAndInteresting;
import org.crews.utils.AESUtil;

import java.util.List;

@Getter
@Builder
public class MyinfoResponse {
    private String email;
    private String nickname;
    private List<InterestResponse> interests;
    private AddressResponse address;


    public static MyinfoResponse from(Member member) {
        return MyinfoResponse.builder()
                .email(AESUtil.decrypt(member.getEmail()))
                .nickname(member.getNickName())
                .interests(
                        member.getMemberAndInterestings().stream()
                                .map(MyinfoResponse::convertToInterestingDto)
                                .toList()
                )
                .address(AddressResponse.from(member.getAddress()))
                .build();
    }

    private static InterestResponse convertToInterestingDto(MemberAndInteresting memberInteresting) {
        return InterestResponse.from(memberInteresting.getInteresting());
    }
}
