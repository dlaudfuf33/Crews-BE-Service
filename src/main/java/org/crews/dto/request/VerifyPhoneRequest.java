package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerifyPhoneRequest {

    @NotBlank(message = "핸드폰 번호는 필수 입력 항목입니다.")
    private String phoneNumber;
}
