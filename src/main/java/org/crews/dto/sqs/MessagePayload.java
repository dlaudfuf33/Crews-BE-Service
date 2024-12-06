package org.crews.dto.sqs;

import lombok.*;
import org.crews.dto.core.TransferResponse;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MessagePayload {
    private Long memberId;
    private TransferResponse data;
    private String message;
}
