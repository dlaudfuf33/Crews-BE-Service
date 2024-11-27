package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

public class AgitRegisterRequest {
    @NotBlank
    private Long memberId;

    @NotBlank
    private Long agitId;
}
