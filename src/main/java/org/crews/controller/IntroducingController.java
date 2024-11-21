package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.response.IntroducingResponse;
import org.crews.service.IntroducingService;
import org.crews.utils.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/agits/{agits-id}/introducing")
public class IntroducingController {
    private final IntroducingService introducingService;
    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<IntroducingResponse> getIntroducing(
            @PathVariable("agits-id") Long agitsId,
            HttpServletRequest request
    ) {
        Long memberId = authUtil.getMemberId(request);

        return ResponseEntity.ok().body(introducingService.getIntroducing(memberId, agitsId));
    }

}
