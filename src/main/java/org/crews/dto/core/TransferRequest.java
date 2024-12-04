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
public class TransferRequest {

    /**
     * 출금 계좌의 핀테크 이용 번호
     */
    private String finUseNum;

    /**
     * 입금할 계좌 번호
     */
    private String recvAccountNum;

    /**
     * 이체할 금액
     */
    private BigDecimal amt;

    /**
     * 이체 설명
     */
    private String description;

}