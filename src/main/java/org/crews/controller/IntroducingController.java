package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.IntroducingRequest;
import org.crews.dto.response.IntroducingResponse;
import org.crews.service.IntroducingService;
import org.crews.utils.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/agits/{agits-id}/introducing")
public class IntroducingController {
    private final IntroducingService introducingService;
    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<IntroducingResponse> getIntroducing(@PathVariable("agits-id") Long agitsId) {
        return ResponseEntity.ok().body(introducingService.getIntroducing(agitsId));
    }

    @GetMapping("/edit")
    public ResponseEntity<IntroducingResponse> getIntroducingForEdit(@PathVariable("agits-id") Long agitsId) {
        return ResponseEntity.ok().body(introducingService.getIntroducing(agitsId));
    }

    @PatchMapping("/edit")
    public ResponseEntity<IntroducingResponse> updateIntroducing(
            @PathVariable("agits-id") Long agitsId,
            @RequestBody IntroducingRequest introducingRequest,
            HttpServletRequest request
    ){
        Long memberId = authUtil.getMemberId(request);

        return ResponseEntity.ok().body(introducingService.updateIntroducing(memberId, agitsId, introducingRequest));
    }
}
