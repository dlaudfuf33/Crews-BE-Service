package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AgitSortResponse {
    private List<AgitResponse> recruitList;
    private List<AgitResponse> newAgitList;
}
