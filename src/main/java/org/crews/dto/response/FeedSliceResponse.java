package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.crews.model.Feed;
import org.crews.model.Member;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class FeedSliceResponse {
    private boolean hasNext;
    private List<FeedResponse> data;

    public static FeedSliceResponse of(Member member, Slice<Feed> feeds) {
        boolean hasNext = feeds.hasNext();
        List<FeedResponse> feedResponse = feeds.getContent().stream()
                .map(feed -> FeedResponse.of(member, feed))
                .collect(Collectors.toList());

        return new FeedSliceResponse(hasNext, feedResponse);
    }
}
