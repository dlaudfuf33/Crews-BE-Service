package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.crews.model.Meeting;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
public class MeetingResponse {
    private Long id;
    private String image;
    private String name;
    private String place;
    private LocalDateTime date;
    private String content;

    public static MeetingResponse from(Meeting meeting) {
        return new MeetingResponse(
                meeting.getId(),
                meeting.getImage(),
                meeting.getRegularName(),
                meeting.getPlace(),
                meeting.getRegularTime(),
                meeting.getContent()
        );
    }
}
