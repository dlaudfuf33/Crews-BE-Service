package org.crews.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.AccountResponse;
import org.crews.dto.request.*;
import org.crews.dto.response.*;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.service.MemberService;
import org.crews.utils.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final AuthUtil authUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody MemberRequest memberRequest) {
        try {
            MemberResponse memberResponse = memberService.signUp(memberRequest);
            if (memberResponse != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Already Exist Email");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during registration");
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<ReissueResponse> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        String accessTokenName = "access";
        String refreshTokenName = "refresh";
        Cookie[] cookies = request.getCookies();
        try {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(refreshTokenName)) {
                    refresh = cookie.getValue();
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ReissueResponse("유효하지 않은 요청입니다."));
        }

        ResponseEntity<String> responseEntity = memberService.refreshCheck(refresh);
        if (responseEntity != null) {
            return ResponseEntity.status(responseEntity.getStatusCode())
                    .body(new ReissueResponse("유효하지 않은 토큰입니다."));
        }

        Map<String, String> tokens = memberService.reissueTokens(refresh);

        String accessToken = tokens.get(accessTokenName);
        String refreshToken = tokens.get(refreshTokenName);

        // Set headers and cookies
        response.setHeader(accessTokenName, accessToken);

        Cookie cookie = new Cookie(refreshTokenName, refreshToken);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        // Return JSON response
        return ResponseEntity.ok(new ReissueResponse("재발행이 성공하였습니다."));
    }


    @PostMapping("/signup/validate-email")
    public ResponseEntity<String> validateEmail(@RequestBody EmailRequest request) {
        boolean isExist = memberService.validateEmail(request);
        if (isExist) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("사용 가능한 이메일입니다.");
        }
    }

    @GetMapping("/me/profile")
    public ResponseEntity<MyProfileResponse> getMyProfile(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        MyProfileResponse profile = memberService.getMyProfile(memberId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me/profile")
    public ResponseEntity<Void> updateMyProfile(HttpServletRequest request,
                                                @RequestBody ProfileImageRequest profileImageRequest) {
        Long memberId = authUtil.getMemberId(request);
        memberService.updateMyProfile(memberId, profileImageRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me/profile")
    public ResponseEntity<Void> deleteMyProfile(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        memberService.deletetMyProfile(memberId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/me/nickname")
    public ResponseEntity<MyNicknameResponse> getMyNickname(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        MyNicknameResponse myNickName = memberService.getMyNickname(memberId);
        return ResponseEntity.ok(myNickName);
    }

    @PutMapping("/me/nickname")
    public ResponseEntity<MyNicknameResponse> updateMyNickname(@RequestBody MyNicknameRequest myNicknameRequest, HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok(memberService.updateMyNickname(memberId, myNicknameRequest));
    }

    @GetMapping("/me/interests")
    public ResponseEntity<List<InterestResponse>> getMyInterests(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok(memberService.getMyInterests(memberId));
    }

    @PutMapping("/me/interests")
    public ResponseEntity<Void> updateMyInterests(@RequestBody InterestsUpdateRequest interestsUpdateRequest,
                                                  HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        memberService.updateMyInterestings(memberId, interestsUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MyinfoResponse> getMyInfo(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        MyinfoResponse myInfo = memberService.getMyinfo(memberId);
        return ResponseEntity.ok(myInfo);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> leavCrews(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        log.info("회원탈퇴 요청 시작: 회원ID={}", memberId);
        try {
            memberService.leavCrews(memberId);
            log.info("회원탈퇴 성공: 회원ID={}", memberId);
            return ResponseEntity.noContent().build();
        } catch (CustomException ce) {
            log.error("회원탈퇴 중 커스텀 예외 발생: 회원ID={}, 오류코드={}, 메시={}", memberId, ce.getErrorCode(), ce.getMessage());
            throw ce;
        } catch (Exception e) {
            log.error("회원탈퇴 중 예상치 못한 예외 발생: 회원ID={}, 메시={}", memberId, e.getMessage(), e);
            throw new CustomException(ErrorCode.DATABASE_ACCESS_FAILED, e);
        }
    }

    @GetMapping("/me/addresses")
    public ResponseEntity<AddressResponse> getAddresses(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        AddressResponse myAddresses = memberService.getMyAddresses(memberId);
        return ResponseEntity.ok(myAddresses);
    }

    @PutMapping("/me/addresses")
    public ResponseEntity<Void> updateAddresses(@RequestBody AddressRequest addressRequest, HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        memberService.updateMyAddresses(memberId, addressRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordUpdateRequest passwordUpdateRequest, HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        try {
            // 비밀번호 변경 서비스 호출
            memberService.updatePassword(memberId, passwordUpdateRequest);
            return ResponseEntity.noContent().build();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/me/agits")
    public ResponseEntity<List<AgitResponse>> getAgits(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        List<AgitResponse> agitResponseList = memberService.getMyAgits(memberId);
        return ResponseEntity.ok(agitResponseList);
    }

    @GetMapping("/me/agits-cards")
    public ResponseEntity<List<AgitCardsResponse>> getAgitsCards(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        List<AgitCardsResponse> agitCardsResponseList = memberService.getMyAgitsCards(memberId);
        return ResponseEntity.ok(agitCardsResponseList);
    }

    @DeleteMapping("/me/agits-cards")
    public ResponseEntity<AgitCardsResponse> deleteAgitsCards(
            HttpServletRequest request, @RequestBody CardDeleteRequest cardDeleteRequest) {
        Long memberId = authUtil.getMemberId(request);
        memberService.deleteMyAgitsCards(memberId, cardDeleteRequest);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/me/my-accounts")
    public ResponseEntity<List<AccountsResponse>> getMyAccounts(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        List<AccountsResponse> accountsResponses = memberService.getMyAccounts(memberId);
        return ResponseEntity.ok(accountsResponses);
    }

    @DeleteMapping("/me/my-accounts")
    public ResponseEntity<Void> deleteMyAccounts(@RequestBody AccountDeleteRequest cardDeleteRequest, HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        try {
            memberService.deleteMyAccounts(memberId, cardDeleteRequest);
            return ResponseEntity.noContent().build();
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/me/core-accounts")
    public ResponseEntity<List<AccountResponse>> getAccountInfo(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok(memberService.getAccountInfoFromCore(memberId));
    }

    @PostMapping("/me/core-accounts")
    public ResponseEntity<Void> attachAccountInfo(@RequestBody AttachAccountRequest attachAccountRequest, HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        memberService.attachAccount(memberId, attachAccountRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/agits-accounts")
    public ResponseEntity<AgitAccountInfoListResponse> getAgitsAccountsInfo(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok(memberService.getAgitsAccountsInfo(memberId));
    }


    @GetMapping("/me/account-withdraws")
    public ResponseEntity<List<WithdrawResponse>> getMyAccountWithdrawHisotry(
            HttpServletRequest request,
            @RequestParam Long crewAccountId,
            @RequestParam Long myAccountId) {
        Long memberId = authUtil.getMemberId(request);
        List<WithdrawResponse> withdrawResponse =
                memberService.getwithdraws(memberId, myAccountId, crewAccountId);
        return ResponseEntity.ok(withdrawResponse);
    }

    @PostMapping("/me/fees/payment")
    public ResponseEntity<TransferMsgResponse> paymentFee(HttpServletRequest request,
                                                          @RequestBody PaymentRequest paymentRequest) {
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok(memberService.paymentFee(memberId, paymentRequest));
    }

    @PostMapping("/find-id")
    public ResponseEntity<FindMemberIdResponse> findMemberId(@RequestBody FindMemberRequest findMemberRequest) {
        return ResponseEntity.ok().body(memberService.findMemberId(findMemberRequest));
    }

    @PostMapping("/find-pw")
    public ResponseEntity<String> findMemberId(@RequestBody FindMemberPwRequest findMemberPwRequest) throws Exception {
        memberService.findMemberPw(findMemberPwRequest);
        return ResponseEntity.ok().body("임시 비밀번호가 입력하신 이메일로 전송되었습니다!");
    }


    @PostMapping("/verify-number")
    public ResponseEntity<String> getVerifyNumber(@RequestBody VerifyPhoneRequest verifyPhoneRequest) {
        memberService.getVerifyNumber(verifyPhoneRequest);
        return ResponseEntity.ok().body("인증번호가 발송되었습니다!");
    }

    @PostMapping("/verify-phone")
    public ResponseEntity<String> verifyNumberCheck(@RequestBody VerifyNumberRequest verifyNumberRequest) {
        memberService.verifyNumberCheck(verifyNumberRequest);
        return ResponseEntity.ok().body("인증이 완료되었습니다!");
    }

    @PostMapping("/me/pin-number")
    public ResponseEntity<String> verifyPinNumber(
            @RequestBody @Valid PinNumberRequest pinNumberRequest,
            HttpServletRequest request
    ) {
        Long memberId = authUtil.getMemberId(request);
        memberService.verifyPinNumber(memberId, pinNumberRequest);
        return ResponseEntity.ok().body("인증이 완료되었습니다!");
    }

    @PutMapping("/me/pin-number")
    public ResponseEntity<String> updatePinNumber(
            @RequestBody @Valid PinNumberRequest pinNumberRequest,
            HttpServletRequest request
    ) {
        Long memberId = authUtil.getMemberId(request);
        memberService.updatePinNumber(memberId, pinNumberRequest);
        return ResponseEntity.ok().body("변경이 완료되었습니다!");
    }
}



