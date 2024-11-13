package org.crews.dto.core;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CardIssuedResponse {
    private String cardName;
    private String cardNumber;
    private LocalDateTime createAt;
    private LocalDateTime expiredAt;
    private String accountNumber;

}
