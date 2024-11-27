package org.crews.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
    private List<TransactionHistoryResponse> tranList = new ArrayList<>();
}
