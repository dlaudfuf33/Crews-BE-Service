package org.crews.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberRequest {
    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    private String nickName;

    @Email(message = "유효한 이메일 주소를 입력해주세요.")
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "전화번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^\\d{11}$", message = "전화번호는 10~11자리 숫자여야 합니다.")
    private String phoneNumber;

    @NotBlank(message = "도 주소는 필수 입력 항목입니다.")
    private String addressDo;

    @NotBlank(message = "시 주소는 필수 입력 항목입니다.")
    private String addressSi;

    @NotBlank(message = "구(군) 주소 는 필수 입력 항목입니다.")
    private String addressGuGun;

    @NotBlank(message = "동 주소는 필수 입력 항목입니다.")
    private String addressDong;


    private String profileImage;

}
