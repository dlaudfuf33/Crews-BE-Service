package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
public class AgitAccountInfoResponse {
    private Long agitId;
    private String agitName;
    private Long accountId;
    private String bankImage;
    private String productName;
    private String accountNumber;
    private int payday;
    private BigDecimal ammount;
    private BigDecimal remainingAmount; // 남은 납부 금액
    private boolean isPaid; // 완납 여부

}