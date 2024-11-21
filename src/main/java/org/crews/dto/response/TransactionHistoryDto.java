package org.crews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crews.model.constants.AccountType;
import org.crews.model.constants.TranType;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionHistoryDto {
    private String counterpartyBankCode;
    private String counterpartyAccountNum;
    private TranType tranType;
    private LocalDateTime transactionTime;
    private String description;
    private BigDecimal tranAmount;
    private BigDecimal afterBalanceAmount;
    private String withdrawerName;
}
