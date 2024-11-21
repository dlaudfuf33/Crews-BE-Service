package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDetailResponse {

    private String accountNumber;
    private String productName;
    private BigDecimal balance;
    private String bankCode;
    private String bankName;

    @Builder.Default
    private List<TransactionHistoryDto> tranList = new ArrayList<>();
}
