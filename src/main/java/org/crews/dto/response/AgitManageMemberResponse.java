package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.crews.model.Member;
import org.crews.model.constants.AgitRole;
import org.crews.utils.AESUtil;

@Builder
@AllArgsConstructor
@Getter
@Slf4j
public class AgitManageMemberResponse {
    private Long id;
    private String name;
    private String nickName;
    private String email;
    private String profileImage;
    private AgitRole agitRole;

    public static AgitManageMemberResponse from(Member member, AgitRole agitRole) {
        return AgitManageMemberResponse.builder()
                .id(member.getId())
                .name(AESUtil.decrypt(member.getName()))
                .nickName(member.getNickName())
                .email(AESUtil.decrypt(member.getEmail()))
                .profileImage(member.getProfileImage())
                .agitRole(agitRole)
                .build();
    }
}
