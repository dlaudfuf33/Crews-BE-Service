package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetailResponse {

    @Builder.Default
    private List<TransactionHistoryDto> tranList = new ArrayList<>();
}
