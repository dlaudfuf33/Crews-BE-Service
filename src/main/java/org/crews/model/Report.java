package org.crews.model;

import jakarta.persistence.*;
import lombok.*;
import org.crews.dto.request.ReportRequest;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Report extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "boolean default false")
    private boolean isChecked;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public static Report of(ReportRequest reportRequest, Feed feed, Member member) {
        return Report.builder()
                .content(reportRequest.getContent())
                .feed(feed)
                .member(member)
                .build();
    }
}
