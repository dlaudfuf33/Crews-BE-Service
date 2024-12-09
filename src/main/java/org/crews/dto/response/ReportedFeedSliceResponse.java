package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.crews.model.Report;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ReportedFeedSliceResponse {
    private boolean hasNext;
    private List<ReportedFeedResponse> data;

    public static ReportedFeedSliceResponse of(Slice<Report> reports) {
        boolean hasNext = reports.hasNext();
        List<ReportedFeedResponse> reportedFeedResponse
                = reports.stream()
                .map(ReportedFeedResponse::of)
                .toList();
        return new ReportedFeedSliceResponse(hasNext, reportedFeedResponse);
    }
}
