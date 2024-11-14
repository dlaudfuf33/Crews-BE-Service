package org.crews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crews.model.Interesting;

@Getter
@Builder
public class InterestingResponseDto {
    private Long interestingId;
    private String name;
    private String subjectName;

    public static InterestingResponseDto of(Interesting interesting) {
        return InterestingResponseDto.builder()
                .interestingId(interesting.getId())
                .name(interesting.getName())
                .subjectName(interesting.getSubject().getSubjectName())
                .build();
    }
}
