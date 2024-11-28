package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerifyNumberRequest extends VerifyPhoneRequest{

    @NotBlank(message = "인증번호는 필수 입력 항목입니다.")
    private String verifyNumber;
}
