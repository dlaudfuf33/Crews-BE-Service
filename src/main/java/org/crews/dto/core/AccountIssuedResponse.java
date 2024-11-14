package org.crews.dto.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crews.model.Account;
import org.crews.model.constants.AccountType;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountIssuedResponse {
    private String memberName;
    private String ci;
    private AccountType accountType;
    private String bankCode;
    private String bankName;
    private String accountNumber;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private BigDecimal balance;
    private String fintechUseNum;


    public static AccountIssuedResponse from(Account account){
        return AccountIssuedResponse.builder()
                .memberName(account.getMember().getName())
                .ci(account.getMember().getCi())
                .accountType(account.getAccountType())
                .bankCode(account.getBank().getBankCode())
                .bankName(account.getBank().getBankName())
                .accountNumber(account.getAccountNumber())
                .createAt(account.getCreatedAt())
                .updateAt(account.getUpdatedAt())
                .balance(account.getBalance())
                .fintechUseNum(account.getFintecNumber())
                .build();
    }
}
