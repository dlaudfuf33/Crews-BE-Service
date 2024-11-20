package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.crews.model.Address;
import org.crews.model.Member;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class MemberResponse {
    private Long id;
    private String name;
    private String nickName;
    private String email;
    private String phoneNumber;
    private String profileImage;
    private List<Address> address;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .name(member.getName())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .profileImage(member.getProfileImage())
                .address(member.getAddresses().stream().toList())
                .build();
    }
}
