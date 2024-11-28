package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FindMemberPwRequest extends FindMemberRequest{
    @NotBlank(message = "유저 아이디는 필수 입력 항목입니다.")
    private String email;
}
