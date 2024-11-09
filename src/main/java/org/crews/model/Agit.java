package org.crews.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
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

    @OneToMany(mappedBy = "agit")
    @Builder.Default
    List<Membership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "agit")
    @Builder.Default
    List<InterestingAndAgit> interestingAndAgits = new ArrayList<>();

    @OneToMany(mappedBy = "agit")
    @Builder.Default
    List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "agit")
    @Builder.Default
    List<Introducing> introducings = new ArrayList<>();

    @OneToMany(mappedBy = "agit")
    @Builder.Default
    List<RegularCrewing> regularCrewings = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Dues dues;

    @OneToMany(mappedBy = "agit")
    @Builder.Default
    private List<AgitAndAccount> agitAndAccounts = new ArrayList<>();

}
