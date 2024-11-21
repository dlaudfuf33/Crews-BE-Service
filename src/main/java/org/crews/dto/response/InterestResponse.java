package org.crews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crews.model.Interesting;

@Getter
@Builder
public class InterestResponse {
    private Long interestingId;
    private String name;

    public static InterestResponse from(Interesting interesting) {
        return InterestResponse.builder()
                .interestingId(interesting.getId())
                .name(interesting.getName())
                .build();
    }
}
