package org.crews.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.crews.dto.request.MemberRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16)
    private String nickName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String pinNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    private String profileImage;

    @Column(nullable = false, length = 88)
    private String ci;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    @ManyToOne(optional = true,fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = true)
    @JsonManagedReference
    private Address address;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Membership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private Set<MemberAndInteresting> memberAndInterestings = new HashSet<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Card> cards = new ArrayList<>();

    public static Member from(MemberRequest memberRequest) {
        return Member.builder()
                .nickName(memberRequest.getNickName())
                .email(memberRequest.getEmail())
                .password(memberRequest.getPassword())
                .name(memberRequest.getName())
                .phoneNumber(memberRequest.getPhoneNumber())
                .profileImage(memberRequest.getProfileImage())
                .build();
    }

    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = "ROLE_USER";
        }
    }

    public void addAccount(Account account) {
        accounts.add(account);
        account.setMember(this);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
        account.setMember(null);
    }
}
