package org.crews.dto.request;

import lombok.Getter;
import java.util.List;

@Getter

public class AddressesRequest {
    private List<AddressRequest> addresses;

}
