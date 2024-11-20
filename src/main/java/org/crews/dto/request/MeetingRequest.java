package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingRequest {
    private String image;

    @NotBlank(message = "정기모임 이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "정기모임 위치는 필수 입력 항목입니다.")
    private String place;

    @NotBlank(message = "정기모임 날짜는 필수 입력 항목입니다.")
    private LocalDateTime date;

    @NotBlank(message = "정기모임 유의사항은 필수 입력 항목입니다.")
    private String content;
}
