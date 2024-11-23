package org.crews.service;


import lombok.RequiredArgsConstructor;
import org.crews.model.Address;
import org.crews.repository.AddressRepository;
import org.crews.utils.AddressUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    /**
     * 주어진 주소 정보를 기반으로 기존 주소를 찾거나, 없으면 새로 생성하여 반환합니다.
     *
     * @param addressDo    도
     * @param addressSi    시
     * @param addressGuGun 구/군
     * @param addressDong  동
     * @return Address 객체
     */
    @Transactional
    public Address findOrCreateAddress(String addressDo, String addressSi, String addressGuGun, String addressDong) {
        String uniqueKey = AddressUtils.generateUniqueAddressKey(addressDo, addressSi, addressGuGun, addressDong);

        // uniqueAddressKey를 사용하여 주소 조회
        return addressRepository.findByUniqueAddressKey(uniqueKey)
                .orElseGet(() -> {
                    // 주소가 없으면 새로 생성하여 저장
                    Address address = Address.builder()
                            .addressDo(addressDo)
                            .addressSi(addressSi)
                            .addressGuGun(addressGuGun)
                            .addressDong(addressDong)
                            .build();
                    return addressRepository.save(address);
                });
    }
}
