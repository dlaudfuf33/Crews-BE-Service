package org.crews.model;

import jakarta.persistence.*;
import lombok.*;
import org.crews.dto.request.MeetingRequest;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Meeting extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Agit agit;

    private String image;

    @Column(nullable = false)
    private String regularName;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private LocalDateTime regularTime;

    @Column(nullable = false)
    private String content;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    public static Meeting of(MeetingRequest meetingRequest, Agit agit){
        return Meeting.builder()
                .agit(agit)
                .image(meetingRequest.getImage())
                .regularName(meetingRequest.getName())
                .place(meetingRequest.getPlace())
                .regularTime(meetingRequest.getDate())
                .content(meetingRequest.getContent())
                .build();
    }

    public void update(MeetingRequest meetingRequest){
        this.regularName = meetingRequest.getName();
        this.image=meetingRequest.getImage();
        this.regularTime=meetingRequest.getDate();
        this.place=meetingRequest.getPlace();
        this.content=meetingRequest.getContent();
    }
}
