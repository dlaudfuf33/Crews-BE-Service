package org.crews.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WithdrawRequest {
    @NotNull
    private Long accountId;

    @NotNull
    private String agitAccountNumber;
}
