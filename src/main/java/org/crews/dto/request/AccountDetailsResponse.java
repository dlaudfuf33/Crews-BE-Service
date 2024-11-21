package org.crews.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDetailsResponse {
    @NotBlank
    private Long memberId;

    @NotBlank
    private String fintechUseNum;

    @NotBlank
    @Max(9)
    private Integer selectPeriod;

    @NotBlank
    private String transactionType;

    @NotBlank
    private String order;
}
