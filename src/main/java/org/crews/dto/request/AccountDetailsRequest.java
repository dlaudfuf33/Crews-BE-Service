package org.crews.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDetailsRequest {
    @NotBlank
    @Max(9)
    private Integer selectPeriod;

    @NotBlank
    private String transactionType;

    @NotBlank
    private String order;
}
