package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class AgitInfoRequest {

    @NotBlank
    private Long agitId;
}
