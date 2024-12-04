package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AgitRequest {
    @NotBlank
    private String introduction;
    @NotBlank
    private Long subject;
    @NotBlank
    private List<Long> interests;
    @NotBlank
    private String name;
    @NotBlank
    private AddressRequest addressRequest;
}
