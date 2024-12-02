package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.crews.model.Agit;
import org.springframework.data.domain.Slice;

import java.util.List;

@Getter
@AllArgsConstructor
public class AgitSliceResponse {
    private boolean hasNext;
    private List<AgitResponse> data;

    public static AgitSliceResponse of(Slice<Agit> events) {
        boolean hasNext = events.hasNext();
        List<AgitResponse> agitResponses = events.getContent().stream()
                .map(AgitResponse::from)
                .toList();

        return new AgitSliceResponse(hasNext, agitResponses);
    }
}
