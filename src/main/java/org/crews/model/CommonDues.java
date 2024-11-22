package org.crews.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CommonDues  extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer dueDay;

    @Column(nullable = false)
    private BigDecimal dueAmount;

    @OneToMany(mappedBy = "commonDues")
    private List<Dues> duesList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private Agit agit;

}
