package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.crews.model.Report;
import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
public class ReportResponse {
    private Long id;
    private LocalDateTime createdAt;
    private String content;
    public static ReportResponse of(Report report) {
        return new ReportResponse(
                report.getId(),
                report.getCreatedAt(),
                report.getContent()
        );
    }
}
