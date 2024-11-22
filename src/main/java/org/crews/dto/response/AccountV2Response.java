package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountV2Response {
    private List<AccountV2CrewResponse> crewAccountList;

    private List<AccountV2PersonalResponse> personalAccountList;
}
