package org.crews.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgitRequest {
    private Long memberId;
    private String introduction;
    private Long subject;
    private List<Long> interests;
    private String name;
}
