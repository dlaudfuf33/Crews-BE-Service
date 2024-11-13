package org.crews.model;

import jakarta.persistence.*;
import lombok.*;
import org.crews.dto.MemberRequest;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    private String profileImage;

    @Column(nullable = false)
    private String identityCode;

    @Column(nullable = false)
    private String role;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Membership> memberships = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<MemberAndInteresting> memberAndInterestings = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
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
}
