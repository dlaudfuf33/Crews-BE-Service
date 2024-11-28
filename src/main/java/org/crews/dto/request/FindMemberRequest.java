package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindMemberRequest {
    @NotBlank(message = "유저 이름은 필수 입력 항목입니다.")
    private String name;
    @NotBlank(message = "유저 핸드폰 번호는 필수 입력 항목입니다.")
    private String phoneNumber;

}
