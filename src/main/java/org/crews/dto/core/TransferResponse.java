package org.crews.dto.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가
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


    /**
     * 이체 날짜 및 시간
     */
    private LocalDateTime transactionTime;

}