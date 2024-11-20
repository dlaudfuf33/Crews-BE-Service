package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AccountLinkRequest {
    @NotBlank
    private Long memberId;

    @NotBlank
    private String fintechUseNum;

}
