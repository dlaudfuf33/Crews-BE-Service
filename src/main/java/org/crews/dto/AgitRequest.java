package org.crews.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgitRequest {
    private String image;
    private String introduction;
    private String feature;
    private Long subject;
    private List<Long> interests;
    private String name;
}
