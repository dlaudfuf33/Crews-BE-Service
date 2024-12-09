package org.crews.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.response.BanResponse;
import org.crews.dto.response.ReportedFeedResponse;
import org.crews.dto.response.ReportedFeedSliceResponse;
import org.crews.service.AdminService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/reported/feeds")
    public ReportedFeedSliceResponse getReportedFeed(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(required = false) Boolean isChecked
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));
        return adminService.getReports(pageable, isChecked);
    }

    @GetMapping("/reported/feeds/{id}")
    public ReportedFeedResponse getReportedFeedDetail(@PathVariable Long id) {
        return adminService.getReportDetailAndMarkAsChecked(id);
    }

    @PostMapping("/ban")
    public BanResponse banUser(@RequestParam Long memberId) {
        return adminService.banMember(memberId);
    }

}
