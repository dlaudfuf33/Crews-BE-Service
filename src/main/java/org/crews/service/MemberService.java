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

    List<AccountResponse> getAccountInfoFromCore(Long id);

    MyProfileResponse getMyProfile(Long memberId);

    MyinfoResponse getMyinfo(Long memberId);

    List<InterestResponse> getMyInterests(Long memberId);

    boolean validateEmail(EmailRequest request);

    MyNicknameResponse getMyNickname(Long memberId);

    MyNicknameResponse updateMyNickname(Long memberId, MyNicknameRequest myNicknameRequest);

    void updatePassword(Long memberId, PasswordUpdateRequest passwordUpdateRequest);

    void updateMyInterestings(Long memberId, InterestsUpdateRequest interestsUpdateRequest);

    AddressResponse getMyAddresses(Long memberId);

    void updateMyAddresses(Long memberId, AddressRequest addressRequest);

    List<AgitResponse> getMyAgits(Long memberId);

    List<AgitCardsResponse> getMyAgitsCards(Long memberId);

    void deleteMyAgitsCards(Long memberId, CardDeleteRequest cardDeleteRequest);

    List<AccountsResponse> getMyAccounts(Long memberId);
}
