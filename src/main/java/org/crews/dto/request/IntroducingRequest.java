package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroducingRequest {
    private String image;

    @NotBlank(message = "한줄 소개는 필수 입력 항목입니다.")
    private String introduce;

    private String content;

    private List<Long> deleteInterests;
    private List<Long> addInterests;
}
