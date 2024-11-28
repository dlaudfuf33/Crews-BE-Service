package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.crews.model.Member;
import org.crews.utils.AESUtil;

@Getter
@ToString
@AllArgsConstructor
public class FindMemberIdResponse {
    private String email;

    public static FindMemberIdResponse from(Member member) {
        return new FindMemberIdResponse(AESUtil.decrypt(member.getEmail()));
    }
}
