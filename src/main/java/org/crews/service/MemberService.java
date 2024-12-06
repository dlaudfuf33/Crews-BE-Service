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

    void deleteMyAccounts(Long memberId, AccountDeleteRequest cardDeleteRequest);

    void attachAccount(Long memberId, AttachAccountRequest attachAccountRequest);

    FindMemberIdResponse findMemberId(FindMemberRequest findMemberRequest);

    void findMemberPw(FindMemberPwRequest findMemberPwRequest) throws Exception;

    void getVerifyNumber(VerifyPhoneRequest verifyPhoneRequest);

    void verifyNumberCheck(VerifyNumberRequest verifyNumberRequest);

    void deleteVerifyMessages();

    AgitAccountInfoListResponse getAgitsAccountsInfo(Long memberId);

    List<WithdrawResponse> getwithdraws(Long memberId, Long myAccountId, Long crewAccountId);

    TransferMsgResponse paymentFee(Long memberId, PaymentRequest paymentRequest);

    void verifyPinNumber(Long memberId, PinNumberRequest pinNumberRequest);

    void updatePinNumber(Long memberId, PinNumberRequest pinNumberRequest);

    void updateMyProfile(Long memberId, ProfileImageRequest profileImageRequest);

    void deletetMyProfile(Long memberId);

    void leavCrews(Long memberId,LeavRequest leavRequest);
}
