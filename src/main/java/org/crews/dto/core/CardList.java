package org.crews.dto.core;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CardList {
    private String memberName;
    private String bankCode;
    private String bankName;
    private String accountNumber;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String cardNumber;

}
