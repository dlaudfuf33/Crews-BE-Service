package org.crews.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PaymentRequest {
    private String pinNumber;
    private Long agitId;
    private Long crewAccountId;
    private Long myAccountId;
    private BigDecimal amount;
}
