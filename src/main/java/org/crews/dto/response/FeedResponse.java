package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.crews.model.Feed;
import org.crews.model.Member;
import org.crews.utils.AESUtil;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
public class FeedResponse {
    private Long id;
    private LocalDateTime createdAt;
    private String image;
    private String content;
    private Long likeCount;
    private boolean isDeleted;
    private boolean likeFeed;
    private String name;

    public static FeedResponse of(Member member, Feed feed) {
        boolean likeFeed = feed.getLikes().stream()
                .anyMatch(heart -> heart.getMember().equals(member));
        String decryptedName;
        try {
            decryptedName = AESUtil.decrypt(feed.getMember().getName()); // 복호화 수행
        } catch (AESUtil.AESUtilException e) {
            decryptedName = feed.getMember().getName();
        }

        return new FeedResponse(
                feed.getId(),
                feed.getCreatedAt(),
                feed.getImage(),
                feed.getContent(),
                feed.getLikeCount(),
                feed.isDeleted(),
                likeFeed,
                decryptedName
        );
    }
}
