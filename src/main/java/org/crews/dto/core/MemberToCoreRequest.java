package org.crews.dto.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.crews.model.Member;

@Getter
@Setter
@NoArgsConstructor
public class MemberToCoreRequest {
    private String ci;

    public MemberToCoreRequest(String ci) {
        this.ci = ci;
    }

    public static MemberToCoreRequest from(Member member) {
        return new MemberToCoreRequest(member.getCi());
    }
}