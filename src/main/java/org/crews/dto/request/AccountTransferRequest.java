package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountTransferRequest {

    @NotBlank
    private String recvAccountNumber;

    @NotBlank
    private Long accountId;

    @NotBlank
    private BigDecimal amount;

    @NotBlank
    private String pinNumber;

}
