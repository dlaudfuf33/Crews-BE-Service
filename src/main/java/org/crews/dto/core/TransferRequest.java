package org.crews.dto.core;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TransferRequest {
    private String finUseNum;
    private String recvAccountNum;
    private BigDecimal amt;

}
