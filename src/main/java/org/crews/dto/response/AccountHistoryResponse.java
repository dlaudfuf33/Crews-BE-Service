package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.crews.model.Dues;
import org.crews.utils.AESUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountHistoryResponse {

    private String accountNumber;
    private String tranType;
    private BigDecimal dueAmount;
    private LocalDateTime dueDate;
    private String productName;
    private Long agitId;
    private String agitName;

    public static AccountHistoryResponse of(Dues dues, Long agitId){
        return AccountHistoryResponse.builder().accountNumber(AESUtil.decrypt(dues.getAccountNumber()))
                .tranType("출금").dueAmount(dues.getDueAmount()).dueDate(dues.getDueDate())
                .productName(dues.getProductName())
                .agitName(dues.getAgitName()).agitId(agitId).build();
    }
}
