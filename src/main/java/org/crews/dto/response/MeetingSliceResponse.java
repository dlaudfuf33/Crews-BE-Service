package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.crews.model.Meeting;
import org.crews.model.Member;
import org.crews.model.Membership;
import org.crews.model.constants.MemberRole;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class MeetingSliceResponse {
    private MemberRole memberRole;
    private boolean hasNext;
    private List<MeetingResponse> data;

    public static MeetingSliceResponse of(Membership membership, Slice<Meeting> events) {
        MemberRole memberRole = membership.getRole();
        boolean hasNext = events.hasNext();
        List<MeetingResponse> meetingResponse = events.getContent().stream()
                .map(MeetingResponse::from)
                .toList();

        return new MeetingSliceResponse(memberRole, hasNext, meetingResponse);
    }
}
