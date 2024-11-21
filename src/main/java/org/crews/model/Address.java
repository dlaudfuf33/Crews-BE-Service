package org.crews.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.crews.dto.request.MemberRequest;
import org.crews.model.constants.AddressType;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String addressDo;

    @Column(nullable = false)
    private String addressSi;

    @Column(nullable = false)
    private String addressGuGun;

    @Column(nullable = false)
    private String addressDong;

    @Column(nullable = false, insertable = false)
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    public static Address of(Member member, MemberRequest memberRequest) {
        return Address.builder()
                .member(member)
                .addressDo(memberRequest.getAddressDo())
                .addressSi(memberRequest.getAddressSi())
                .addressGuGun(memberRequest.getAddressGuGun())
                .addressDong(memberRequest.getAddressDong())
                .build();
    }
}
