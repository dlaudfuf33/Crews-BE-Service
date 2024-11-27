package org.crews.dto.core;

import lombok.*;
import org.crews.model.constants.AccountType;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountOneResponse {
    private Long accountId;
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
