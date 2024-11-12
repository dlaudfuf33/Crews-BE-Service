package org.crews.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AccountLinkRequest {
    @NotBlank
    private Long memberId;

    @NotBlank
    private String fintechUseNum;

}
