package org.crews.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.EmailRequest;
import org.crews.dto.request.MemberDetails;
import org.crews.dto.request.MemberRequest;
import org.crews.dto.response.InterestingResponse;
import org.crews.dto.response.MemberResponse;
import org.crews.dto.response.MyProfileResponse;
import org.crews.dto.response.MyinfoResponse;
import org.crews.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody MemberRequest memberRequest) {
        try {
            MemberResponse memberResponse = memberService.signUp(memberRequest);
            if(memberResponse != null) {
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
        try{
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
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        response.addCookie(cookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/signup/validate-email")
    public ResponseEntity<String> validateEmail(@RequestBody EmailRequest request) {
        boolean isExist = memberService.validateEmail(request);
        if(isExist) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Already Exist Email");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Email Available For Registration.");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<MyinfoResponse> getMyinfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        MyinfoResponse myInfo = memberService.getMyinfo(memberDetails.getUsername());
        return ResponseEntity.ok(myInfo);
    }

    @GetMapping("/me/profile")
    public ResponseEntity<MyProfileResponse> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        MyProfileResponse profile = memberService.getMyProfile(memberDetails.getUsername());
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/me/interests")
    public ResponseEntity<List<InterestingResponse>> getMyInterestings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        return ResponseEntity.ok(memberService.getMyInterests(memberDetails.getUsername()));
    }
}



