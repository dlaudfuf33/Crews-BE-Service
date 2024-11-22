package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.crews.model.Agit;
import org.crews.model.Member;
import org.crews.model.Membership;

@Getter
@AllArgsConstructor
public class AgitVaildationResponse {
    private final Agit agit;
    private final Member member;
    private final Membership membership;
}
