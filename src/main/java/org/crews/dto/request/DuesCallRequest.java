package org.crews.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DuesCallRequest {

    @NotNull
    private List<Long> memberId = new ArrayList<>();

    @NotNull
    private BigDecimal duesAmount;

    @NotNull
    private Integer year;

    @NotNull
    private Integer month;
}
