package org.crews.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CardDeleteRequest {
    @NotNull
    private Long cardId;
}
