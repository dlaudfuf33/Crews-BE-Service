package org.crews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crews.model.Subject;

import java.util.List;

@Getter
@Builder
public class SubjectResponse {
    private Long subjectId;
    private String subjectName;
    private List<InterestResponse> interests;

    public static SubjectResponse from(Subject subject) {
        return SubjectResponse.builder()
                .subjectId(subject.getId())
                .subjectName(subject.getSubjectName())
                .interests(
                        subject.getInterestings().stream()
                                .map(InterestResponse::from)
                                .toList()
                )
                .build();
    }
}
