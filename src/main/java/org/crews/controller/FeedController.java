package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.response.FeedSliceResponse;
import org.crews.service.FeedService;
import org.crews.utils.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/agits/{agits-id}/feeds")
public class FeedController {

    private final FeedService feedService;
    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<FeedSliceResponse> getAllFeeds(
            @PathVariable("agits-id") Long agitId,
            @RequestParam int page,
            HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        System.out.println("memberId = " + memberId);
        return ResponseEntity.ok().body(feedService.getAllFeeds(memberId, agitId, page));
    }
}
