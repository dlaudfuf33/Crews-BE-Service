package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.ReportRequest;
import org.crews.dto.response.ReportResponse;
import org.crews.service.ReportService;
import org.crews.utils.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/agits/{agits-id}/feeds")
public class ReportController {
    private final AuthUtil authUtil;
    private final ReportService reportService;

    @PostMapping("/{feed-id}")
    public ResponseEntity<ReportResponse> reportFeed(
            @PathVariable("agits-id") Long agitId,
            @PathVariable("feed-id") Long feedId,
            @RequestBody ReportRequest reportRequest, HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(reportService.reportFeed(memberId, feedId, agitId, reportRequest));
    }
}
