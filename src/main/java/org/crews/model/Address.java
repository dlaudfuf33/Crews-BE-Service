package org.crews.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.crews.utils.AddressUtils;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private List<Member> memberList = new ArrayList<>();

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Agit> agitList = new ArrayList<>();

    @Column(nullable = false)
    private String addressDo;

    @Column(nullable = false)
    private String addressSi;

    @Column(nullable = false)
    private String addressGuGun;

    @Column(nullable = false)
    private String addressDong;

    @Column(nullable = false, unique = true)
    private String uniqueAddressKey;

    /**
     * 주소가 저장되기 전에 고유 키를 생성합니다.
     */
    @PrePersist
    public void generateUniqueAddressKey() {
        this.uniqueAddressKey = AddressUtils.generateUniqueAddressKey(addressDo, addressSi, addressGuGun, addressDong);
    }

    /**
     * 주소가 업데이트되기 전에 고유 키를 재생성합니다.
     */
    @PreUpdate
    public void updateUniqueAddressKey() {
        this.uniqueAddressKey = AddressUtils.generateUniqueAddressKey(addressDo, addressSi, addressGuGun, addressDong);
    }
}
