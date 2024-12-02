package org.crews.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "정기모임 날짜는 필수 입력 항목입니다.")
    @Future(message="현재 시간 이후로 설정해주세요.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime date;

    @NotBlank(message = "정기모임 유의사항은 필수 입력 항목입니다.")
    private String content;
}
