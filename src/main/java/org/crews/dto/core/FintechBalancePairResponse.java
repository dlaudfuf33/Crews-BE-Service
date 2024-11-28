package org.crews.dto.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class FintechBalancePairResponse {
    private String fintechNumber;
    private BigDecimal balance;
}
