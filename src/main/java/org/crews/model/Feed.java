package org.crews.model;

import jakarta.persistence.*;
import lombok.*;
import org.crews.dto.request.FeedRequest;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Feed extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String image;

    @Column(nullable = false)
    private String content;

    @ColumnDefault("0")
    private Long likeCount;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    private Agit agit;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "feed")
    @Builder.Default
    private List<Heart> likes = new ArrayList<>();

    public static Feed of(FeedRequest feedRequest, Agit agit, Member member) {
        return Feed.builder()
                .image(feedRequest.getImage())
                .content(feedRequest.getContent())
                .likeCount(0L)
                .isDeleted(false)
                .agit(agit)
                .member(member)
                .build();
    }

    public void update(FeedRequest feedRequest) {
        this.image = feedRequest.getImage();
        if (feedRequest.getContent() != null && !feedRequest.getContent().isEmpty()) {
            this.content = feedRequest.getContent();
        }

    }
}
