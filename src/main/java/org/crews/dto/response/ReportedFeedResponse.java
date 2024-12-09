package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.crews.model.Report;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReportedFeedResponse {
    private Long id;
    private Long feedId;
    private Long memberId;
    private String feedImage;
    private String feedContent;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isChecked;

    public static ReportedFeedResponse of(Report report) {
        return ReportedFeedResponse.builder()
                .id(report.getId())
                .feedId(report.getFeed().getId())
                .memberId(report.getMember().getId())
                .feedImage(report.getFeed().getImage())
                .feedContent(report.getFeed().getContent())
                .content(report.getContent())
                .createdAt(report.getCreatedAt())
                .updatedAt(report.getUpdatedAt())
                .isChecked(report.isChecked())
                .build();
    }
}
