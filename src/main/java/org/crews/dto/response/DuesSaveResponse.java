package org.crews.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DuesSaveResponse {

    @NotBlank
    private BigDecimal dueAmount;

    @NotBlank
    private Integer dueDay;
}
