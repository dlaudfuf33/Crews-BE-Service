package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.crews.model.Meeting;
import java.time.format.DateTimeFormatter;

@Getter
@ToString
@AllArgsConstructor
public class MeetingResponse {
    private Long id;
    private String image;
    private String name;
    private String place;
    private String date;
    private String content;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static MeetingResponse from(Meeting meeting) {
        return new MeetingResponse(
                meeting.getId(),
                meeting.getImage(),
                meeting.getRegularName(),
                meeting.getPlace(),
                meeting.getRegularTime().format(formatter),
                meeting.getContent()
        );
    }
}
