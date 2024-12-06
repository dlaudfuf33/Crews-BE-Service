package org.crews.dto.core;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crews.model.Account;
import org.crews.model.constants.TranType;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WithdrawTransactionRequest {

    @NotBlank
    private String ci;

    @NotBlank
    private String fintechUseNum;

    private final Integer selectPeriod = 1;

    @NotBlank
    private final String transactionType = TranType.WITHDRAW.toString();

    @NotBlank
    private final String order = "DESC";

    public static WithdrawTransactionRequest from(Account account){
        return WithdrawTransactionRequest.builder()
                .ci(account.getMember().getCi())
                .fintechUseNum(account.getFintecNumber())
                .build();

    }
}
