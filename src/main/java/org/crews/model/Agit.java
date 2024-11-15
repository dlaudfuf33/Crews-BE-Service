package org.crews.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Agit extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String agitName;

    @ColumnDefault("10")
    private Integer maxPerson;

    @ColumnDefault("1")
    private Integer currentPerson;

    @Column(nullable = false)
    private boolean isDue;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    @Builder.Default
    @OneToMany(mappedBy = "agit")
    private List<Membership> memberships = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "agit")
    private List<InterestingAndAgit> interestingAndAgits = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "agit")
    private List<Feed> feeds = new ArrayList<>();

    @OneToOne(mappedBy = "agit")
    private Introducing introducing;

    @Builder.Default
    @OneToMany(mappedBy = "agit")
    private List<RegularCrewing> regularCrewings = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Dues dues;

    @OneToOne()
    private AgitAndAccount agitAndAccount;

    @OneToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
}