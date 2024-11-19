package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.crews.model.Meeting;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class MeetingSliceResponse {
    private boolean hasNext;
    private List<MeetingResponse> data;

    public static MeetingSliceResponse from(Slice<Meeting> events) {
        List<MeetingResponse> meetingResponse = events.getContent().stream()
                .map(MeetingResponse::from)
                .collect(Collectors.toList());
        boolean hasNext = events.hasNext();

        return new MeetingSliceResponse(hasNext, meetingResponse);
    }
}
