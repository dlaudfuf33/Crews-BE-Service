package org.crews.dto.core;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceInfoRequest {

    @NotBlank
    private String fintecUseNum;

    @NotBlank
    private String recvFintecUseNum;
}
