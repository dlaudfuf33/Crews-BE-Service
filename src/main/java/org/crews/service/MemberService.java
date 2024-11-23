package org.crews.service;

import org.crews.dto.core.AccountResponse;
import org.crews.dto.request.*;
import org.crews.dto.response.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface MemberService {

    MemberResponse signUp(MemberRequest memberRequest);

    ResponseEntity<String> refreshCheck(String refresh);

    Map<String, String> reissueTokens(String refresh);

    public List<AccountResponse> getAccountInfoFromCore(Long id);

    public MyProfileResponse getMyProfile(Long memberId);

    public MyinfoResponse getMyinfo(Long memberId);

    public List<InterestResponse> getMyInterests(Long memberId);

    boolean validateEmail(EmailRequest request);

    MyNicknameResponse getMyNickname(Long memberId);

    MyNicknameResponse updateMyNickname(Long memberId, MyNicknameRequest myNicknameRequest);


    void updateMyInterestings(Long memberId, InterestsUpdateRequest interestsUpdateRequest);

    AddressesResponse getMyAddresses(Long memberId);

    void updateMyAddresses(Long memberId, AddressesRequest addressesRequest);

    void updatePassword(Long memberId, PasswordUpdateRequest passwordUpdateRequest);
}
