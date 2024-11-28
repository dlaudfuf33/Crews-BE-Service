package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.crews.model.constants.AccountType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class AccountsInfoResponse {
    private int totalCount;
    private List<AccountInfo> accounts;

    @Getter
    @AllArgsConstructor
    public static class AccountInfo {
        private String customerName;
        private String bankCode;
        private String productName;
        private String bankImage;
        private String accountNumber;
        private AccountType accountType;
        private BigDecimal balance;
        private LocalDate createdAt;
        private LocalDate updatedAt;
        private String fintechUseNum;
    }
}