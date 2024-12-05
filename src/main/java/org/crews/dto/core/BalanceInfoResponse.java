package org.crews.dto.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BalanceInfoResponse {

    private String accountNumber;
    private BigDecimal afterBalanceAmt;
    private String recvAccoountNumber;
    private BigDecimal recvAfterBalanceAmt;

}
