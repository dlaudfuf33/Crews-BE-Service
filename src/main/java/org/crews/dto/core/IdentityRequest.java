package org.crews.dto.core;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IdentityRequest {

    @NotBlank
    private String identityCode;
}
