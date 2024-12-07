package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.crews.model.Member;
import org.crews.utils.AESUtil;

@Getter
@AllArgsConstructor
@Builder
public class BanResponse {
    private Long id;
    private String name;
    private String nickName;
    private String email;
    private String phoneNumber;
    private boolean isBanned;

    public static BanResponse from(Member member) {
        return BanResponse.builder()
                .id(member.getId())
                .name(AESUtil.decrypt(member.getName()))
                .nickName(member.getNickName())
                .email(AESUtil.decrypt(member.getEmail()))
                .phoneNumber(AESUtil.decrypt(member.getPhoneNumber()))
                .isBanned(member.isBanned())
                .build();
    }
}
