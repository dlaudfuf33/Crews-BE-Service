package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter

public class MyNicknameRequest {
    @NotBlank
    private String nickname;
}
