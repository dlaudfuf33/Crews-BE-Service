package org.crews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crews.model.Address;
import org.crews.model.constants.AddressType;

@Getter
@Builder
public class AddressResponse {
    private String doName;
    private String siName;
    private String guName;
    private String dongName;

    public static AddressResponse from(Address address) {
        return AddressResponse.builder()
                .doName(address.getAddressDo())
                .siName(address.getAddressSi())
                .guName(address.getAddressGuGun())
                .dongName(address.getAddressDong())
                .build();
    }
}
