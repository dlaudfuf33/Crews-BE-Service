package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WithdrawSliceResponse {
    private boolean hasNext;
    private List<WithdrawResponse> data;
}