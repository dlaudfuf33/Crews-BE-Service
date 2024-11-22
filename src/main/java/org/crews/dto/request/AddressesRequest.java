package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import java.util.List;

@Getter

public class AddressesRequest {
    @NotBlank
    private List<AddressRequest> addresses;

}
