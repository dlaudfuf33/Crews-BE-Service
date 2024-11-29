package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.crews.model.Meeting;
import org.crews.model.Membership;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@AllArgsConstructor
public class MeetingSliceResponse {
    private boolean hasNext;
    private List<MeetingResponse> data;

    public static MeetingSliceResponse of(Membership membership, Slice<Meeting> events) {
        boolean hasNext = events.hasNext();
        List<MeetingResponse> meetingResponse = events.getContent().stream()
                .map(MeetingResponse::from)
                .toList();

        return new MeetingSliceResponse(hasNext, meetingResponse);
    }
}
