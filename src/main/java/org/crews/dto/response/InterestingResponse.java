package org.crews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crews.model.Interesting;

@Getter
@Builder
public class InterestingResponse {
    private Long interestingId;
    private String name;
    private String subjectName;

    public static InterestingResponse from(Interesting interesting) {
        return InterestingResponse.builder()
                .interestingId(interesting.getId())
                .name(interesting.getName())
                .subjectName(interesting.getSubject().getSubjectName())
                .build();
    }
}
