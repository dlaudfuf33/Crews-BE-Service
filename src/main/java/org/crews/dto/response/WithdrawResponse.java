package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class WithdrawResponse {
    private LocalDateTime trxTime;
    private String agitName;
    private String productName;
    private String accountNumber;
    private BigDecimal amount;


}