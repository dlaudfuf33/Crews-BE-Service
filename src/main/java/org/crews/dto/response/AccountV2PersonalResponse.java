package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crews.model.Account;
import org.crews.model.Agit;
import org.crews.utils.AESUtil;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountV2PersonalResponse {

    private String bankCode;
    private String bankName;
    private String accountNumber;
    private String productName;
    private BigDecimal balance;

    public static AccountV2PersonalResponse from(Account account){
        return AccountV2PersonalResponse
                .builder()
                .bankCode(account.getBank().getBankCode())
                .bankName(account.getBank().getBankName())
                .accountNumber(AESUtil.decrypt(account.getAccountNumber()))
                .productName(account.getProductName())
                .balance(account.getBalance())
                .build();
    }
}
