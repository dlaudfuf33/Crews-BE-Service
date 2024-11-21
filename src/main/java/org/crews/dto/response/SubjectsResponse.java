package org.crews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crews.model.Subject;

import java.util.List;

@Getter
@Builder
public class SubjectsResponse {
    private List<SubjectResponse> subjects;

    public static SubjectsResponse from(List<Subject> subjects) {
        return SubjectsResponse.builder()
                .subjects(
                        subjects.stream()
                                .map(SubjectResponse::from)
                                .toList()
                )
                .build();
    }
}
