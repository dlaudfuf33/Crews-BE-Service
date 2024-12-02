package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PinNumberRequest {
    @NotBlank(message = "핀번호는 필수 입력 항목입니다.")
    private String pinNumber;
}
