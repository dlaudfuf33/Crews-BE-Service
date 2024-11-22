package org.crews.dto.core;

import lombok.Getter;

@Getter
public class ProductResponse {
    private Long id;
    private String bankCode;
    private String bankName;
    private String productName;
    private double highestRate;
    private double lowestRate;

}
