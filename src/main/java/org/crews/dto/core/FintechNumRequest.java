package org.crews.dto.core;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FintechNumRequest {

    @NotEmpty(message = "사용자 CI는 필수입니다.")
    private String ci;

    @NotEmpty(message = "계좌 번호 목록은 필수입니다.")
    @Size(min = 1, message = "계좌 번호는 최소 1개 이상이어야 합니다.")
    private List<@NotEmpty(message = "계좌 번호는 필수입니다.") String> accountNumbers;

}