package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.crews.model.constants.AddressType;

@Getter
public class AddressRequest {
    @NotBlank
    private AddressType type;

    @NotBlank
    private String doName;

    @NotBlank
    private String siName;

    @NotBlank
    private String guName;

    @NotBlank
    private String dongName;

}
