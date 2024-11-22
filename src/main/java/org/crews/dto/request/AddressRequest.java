package org.crews.dto.request;

import lombok.Getter;
import org.crews.model.constants.AddressType;

@Getter
public class AddressRequest {
    private AddressType type;
    private String doName;
    private String siName;
    private String guName;
    private String dongName;

}
