package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CardRemoveRequest {

    @NotBlank
    private Long memberId;

    @NotBlank
    private String fintechUseNum;

    @NotBlank
    private String cardNumber;
}
