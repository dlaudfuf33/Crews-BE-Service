package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crews.model.Member;
import org.crews.utils.AESUtil;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private String name;
    private String nickName;
    private String email;
    private String image;


    public static ProfileResponse from(Member member){
        return ProfileResponse
                .builder()
                .name(AESUtil.decrypt(member.getName()))
                .nickName(member.getNickName())
                .email(AESUtil.decrypt(member.getEmail()))
                .image(member.getProfileImage())
                .build();
    }
}
