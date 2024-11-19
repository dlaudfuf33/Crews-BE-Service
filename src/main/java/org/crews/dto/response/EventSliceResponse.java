package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.crews.model.Event;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class EventSliceResponse {
    private boolean hasNext;
    private List<EventResponse> data;

    public static EventSliceResponse from(Slice<Event> events) {
        List<EventResponse> eventResponses = events.getContent().stream()
                .map(EventResponse::from)
                .collect(Collectors.toList());
        boolean hasNext = events.hasNext();

        return new EventSliceResponse(hasNext, eventResponses);
    }
}
