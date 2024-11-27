package org.crews.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.*;
import org.crews.dto.response.*;
import org.crews.exception.CustomException;
import org.crews.service.MemberService;
import org.crews.utils.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;
        String tokenName = "refresh";
        Cookie[] cookies = request.getCookies();
        try {
            for (Cookie cookie : cookies) {

                if (cookie.getName().equals(tokenName)) {

                    refresh = cookie.getValue();
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cookie is null");
        }

        ResponseEntity<String> responseEntity = memberService.refreshCheck(refresh);

        if (responseEntity != null) {
            return responseEntity;
        }

        Map<String, String> tokens = memberService.reissueTokens(refresh);

        response.setHeader("access", tokens.get("access"));

        Cookie cookie = new Cookie(tokenName, tokens.get(tokenName));
        cookie.setMaxAge(24 * 60 * 60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/signup/validate-email")
    public ResponseEntity<String> validateEmail(@RequestBody EmailRequest request) {
        boolean isExist = memberService.validateEmail(request);
        if (isExist) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Already Exist Email");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Email Available For Registration.");
        }
    }

    @GetMapping("/me/profile")
    public ResponseEntity<MyProfileResponse> getMyProfile(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        MyProfileResponse profile = memberService.getMyProfile(memberId);
        return ResponseEntity.ok(profile);
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
        MyNicknameResponse myNickName = memberService.updateMyNickname(memberId, myNicknameRequest);
        return ResponseEntity.ok(myNickName);
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

    @GetMapping("/me/addresses")
    public ResponseEntity<AddressResponse> getAddresses(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        AddressResponse myAddresses = memberService.getMyAddresses(memberId);
        return ResponseEntity.ok(myAddresses);
    }

    @PutMapping("/me/addresses")
    public ResponseEntity<Void> updateAddresses(@RequestBody AddressRequest addressRequest, HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        try {
            memberService.updateMyAddresses(memberId, addressRequest);
            return ResponseEntity.noContent().build();
        } catch (CustomException e) {
            return ResponseEntity.status(e.getErrorCode().getHttpStatus()).build();
        }
    }

    @PostMapping("/find-id")
    public ResponseEntity<FindMemberIdResponse> findMemberId(@RequestBody FindMemberRequest findMemberRequest){
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
}



