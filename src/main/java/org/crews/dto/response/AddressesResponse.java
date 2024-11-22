package org.crews.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.crews.model.Address;

import java.util.List;

@Getter
@Builder
public class AddressesResponse {
    private List<AddressResponse> addresses;

    public static AddressesResponse from(List<AddressResponse> addrList) {
        return AddressesResponse.builder()
                .addresses(addrList)
                .build();
    }
}
