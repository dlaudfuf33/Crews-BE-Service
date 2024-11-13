package org.crews.dto.core;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonRequest {

    @NotBlank
    private String ci;

    @NotBlank
    private String fintechUseNum;
}
