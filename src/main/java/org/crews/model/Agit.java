package org.crews.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false)
    private String introduction;

    @Column(nullable = false)
    private Integer maxPerson;

    @Column(nullable = false)
    private Integer currentPerson;

    @Column(nullable = false)
    private boolean isDue;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    @Builder.Default
    @OneToMany(mappedBy = "agit")
    private List<Membership> memberships = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "agit",cascade = CascadeType.ALL)
    private List<InterestingAndAgit> interestingAndAgits = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "agit")
    private List<Feed> feeds = new ArrayList<>();

    @OneToOne(mappedBy = "agit",cascade = CascadeType.PERSIST)
    private Introducing introducing;

    @Builder.Default
    @OneToMany(mappedBy = "agit")
    private List<Meeting> meetings = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Dues dues;

    @OneToOne
    private AgitAndAccount agitAndAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToOne(fetch = FetchType.LAZY)
    private CommonDues commonDues;
}