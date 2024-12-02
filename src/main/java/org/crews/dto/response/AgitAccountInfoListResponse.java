package org.crews.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class AgitAccountInfoListResponse {
   private List<AgitAccountInfoResponse> crewAccounts;
}