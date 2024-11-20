package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.crews.model.Feed;
import org.crews.model.Member;

@Getter
@ToString
@AllArgsConstructor
public class FeedResponse {
    private Long id;
    private String image;
    private String content;
    private Long likeCount;
    private boolean likeFeed;

    public static FeedResponse of(Member member, Feed feed) {
        boolean likeFeed = feed.getLikes().stream()
                .anyMatch(heart -> heart.getMember().equals(member));
        return new FeedResponse(
                feed.getId(),
                feed.getImage(),
                feed.getContent(),
                feed.getLikeCount(),
                likeFeed
        );
    }
}
