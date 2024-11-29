package org.crews.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttachAccountRequest {
    @NotEmpty(message = "계좌 번호 목록은 필수입니다.")
    private List<@NotEmpty(message = "계좌 번호는 필수입니다.") String> accountNumbers;
}