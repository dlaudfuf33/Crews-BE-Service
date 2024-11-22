package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DateRequest {

    @NotBlank
    private Integer year;

    @NotBlank
    private Integer month;
}
