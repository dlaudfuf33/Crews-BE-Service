package org.crews.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event extends BaseTimeEntity{
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
    private String placeAddress;

    @Column(nullable = false)
    private LocalDateTime regularTime;

    @Column(nullable = false)
    private String content;

    @ColumnDefault("1")
    private Integer currentPerson;

    @ColumnDefault("10")
    private Integer maxPerson;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;
}
