package org.crews.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class AgitAuthorRequest {

    @NotBlank
    private String status;
    @NotBlank
    private Long requestMemberId;
}
