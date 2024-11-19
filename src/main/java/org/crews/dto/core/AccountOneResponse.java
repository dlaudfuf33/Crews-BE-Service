package org.crews.dto.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crews.model.constants.AccountType;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountOneResponse {
    private String memberName;
    private String ci;
    private AccountType accountType;
    private String bankCode;
    private String bankName;
    private String accountNumber;
    private String fintechUseNum;
    private String productName;
    private BigDecimal balance;

}
