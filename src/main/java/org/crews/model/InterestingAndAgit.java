package org.crews.model;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InterestingAndAgit extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interesting_id")
    private Interesting interesting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agit_id")
    private Agit agit;
}
