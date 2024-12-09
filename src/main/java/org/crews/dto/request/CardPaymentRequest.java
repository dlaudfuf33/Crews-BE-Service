package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

@Getter
@Builder
@ToString
public class CardPaymentRequest {
    @NotBlank
    private Long agitId;

    @NotBlank
    private String pinNumber;
}
