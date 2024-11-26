package org.crews.dto.response;

import lombok.Getter;

import java.math.BigDecimal;
@Getter
public class AccountsResponse {
    private final Long accountId;
    private final String bankImage;
    private final String bankCode;
    private final String accountName;
    private final String accountNumber;
    private final BigDecimal balance;

    public AccountsResponse(Long accountId, String bankImage, String bankCode, String accountName, String accountNumber, BigDecimal balance) {
        this.accountId = accountId;
        this.bankImage = bankImage;
        this.bankCode = bankCode;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
}
