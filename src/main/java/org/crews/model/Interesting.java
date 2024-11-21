package org.crews.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Interesting extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    @JsonBackReference
    private Subject subject;

    @OneToMany(mappedBy = "interesting")
    @Builder.Default
    private List<MemberAndInteresting> memberAndInterestings = new ArrayList<>();

    @OneToMany(mappedBy = "interesting")
    @Builder.Default
    private List<InterestingAndAgit> interestingAndAgits = new ArrayList<>();
}
