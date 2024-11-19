package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.crews.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class EventResponse {
    private Long id;
    private String image;
    private String name;
    private String place;
    private LocalDateTime date;
    private String content;

    public static EventResponse from(Event event) {
        return new EventResponse(
                event.getId(),
                event.getImage(),
                event.getRegularName(),
                event.getPlace(),
                event.getRegularTime(),
                event.getContent()
        );
    }
}
