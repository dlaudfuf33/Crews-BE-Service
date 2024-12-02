package org.crews.dto.core;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TransferResponse {

    /**
     * 거래의 고유 식별자 (거래 내역 ID)
     */
    private Long historyId;

    /**
     * 수신자의 이름
     */
    private String recvName;

    /**
     * 수신자의 은행 코드
     */
    private String recvBankcode;

    /**
     * 수신자의 계좌 번호
     */
    private String recvAccountNum;

    /**
     * 이체된 금액
     */
    private BigDecimal amount;

    /**
     * 이체 후 남은 잔액 (출금 계좌)
     */
    private BigDecimal afterAmt;

}