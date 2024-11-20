package org.crews.service;

import org.crews.dto.core.AccountResponse;
import org.crews.dto.request.EmailRequest;
import org.crews.dto.request.MemberRequest;
import org.crews.dto.response.InterestingResponse;
import org.crews.dto.response.MemberResponse;
import org.crews.dto.response.MyProfileResponse;
import org.crews.dto.response.MyinfoResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface MemberService {

    MemberResponse signUp(MemberRequest memberRequest);

    ResponseEntity<String> refreshCheck(String refresh);

    Map<String, String> reissueTokens(String refresh);

    public List<AccountResponse> getAccountInfoFromCore(Long id);

    public MyProfileResponse getMyProfile(String memberEmail);

    public MyinfoResponse getMyinfo(String memberEmail);

    public List<InterestingResponse> getMyInterests(String memberEmail);

    boolean validateEmail(EmailRequest request);
}
