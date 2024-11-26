package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.crews.model.Interesting;

@Getter
@ToString
@AllArgsConstructor
public class InterestingResponse {
    private Long id;
    private String name;

    public static InterestingResponse from(Interesting interesting) {
        return new InterestingResponse(
                interesting.getId(),
                interesting.getName()
        );
    }
}
