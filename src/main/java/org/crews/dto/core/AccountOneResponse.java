package org.crews.dto.core;

import lombok.Getter;
import org.crews.model.constants.AccountType;


import java.math.BigDecimal;

@Getter
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
