package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class MessageResponse {
    private String phoneNumber;
    private String messageText;
}
