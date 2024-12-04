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
public class AccountHistoryFinalV2Response {
    private List<AccountHistoryResponse> accountHistory;
}
