package org.crews.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Account extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bank bank;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String maskedAccountNumber;

    @Column(nullable = false)
    private String accountNumber;

    @ColumnDefault("0")
    private BigDecimal balance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    private String fintecNumber;

    @OneToOne(mappedBy = "account")
    private AgitAndAccount agitAndAccount;

    @OneToOne(mappedBy = "account")
    private AccountHistory accountHistory;

    @Builder.Default
    @OneToMany(mappedBy = "account")
    private List<Card> cards = new ArrayList<>();

}
