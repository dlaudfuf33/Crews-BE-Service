package org.crews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crews.model.Member;

@Getter
@Builder
public class MyNicknameResponse {
    private String nickname;

    public static MyNicknameResponse from(Member member) {
        return MyNicknameResponse.builder()
                .nickname(member.getNickName())
                .build();
    }
}
