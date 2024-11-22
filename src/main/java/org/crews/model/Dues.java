package org.crews.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Dues extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal dueAmount;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column(nullable = false)
    private boolean isPayed;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String agitName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Membership membership;

    @ManyToOne(fetch = FetchType.LAZY)
    private CommonDues commonDues;
}
