package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class AgitManageResponse {
    private final List<AgitManageMemberResponse> members;
    private final List<AgitManageMemberResponse> requestedMembers;
    private final Long currentMember;
    private final Long requestedMember;
    private final String message;
}
