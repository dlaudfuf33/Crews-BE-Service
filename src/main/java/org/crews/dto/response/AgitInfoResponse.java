package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crews.model.Membership;
import org.crews.model.constants.AgitRole;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgitInfoResponse {
    private Long agitId;
    private String agitName;
    private AgitRole memberRole;

    public static AgitInfoResponse from(Membership membership){
        return AgitInfoResponse.builder().agitId(membership.getAgit().getId())
                .agitName(membership.getAgit().getAgitName())
                .memberRole(membership.getAgitRole()).build();
    }
}
