package org.crews.dto.core;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferApiResponse {
    private TransferResponse data;
    private ErrorResponse error;

}
