package org.crews.dto.response;

import lombok.*;
import org.crews.model.Membership;
import org.crews.model.constants.AgitRole;
import org.crews.model.constants.CardName;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInfoResponse {
    private String name;
    private Long agitId;
    private AgitRole agitRole;
    private String cardName;
    private String cardCode;
    private String src;
}
