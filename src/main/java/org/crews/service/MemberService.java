package org.crews.service;

import org.crews.dto.MemberRequest;
import org.crews.dto.MemberResponse;
import org.crews.dto.core.AccountResponseDto;
import org.crews.dto.response.InterestingResponseDto;
import org.crews.dto.response.MyProfileResponse;
import org.crews.dto.response.MyinfoResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface MemberService {

    MemberResponse signUp(MemberRequest memberRequest);

    ResponseEntity<String> refreshCheck(String refresh);

    Map<String, String> reissueTokens(String refresh);

    public List<AccountResponseDto> getAccountInfoFromCore(Long id);

    public MyProfileResponse getMyProfile(String memberEmail);


    public MyinfoResponse getMyinfo(String memberEmail);

    public List<InterestingResponseDto> getMyInterests(String memberEmail);
}
