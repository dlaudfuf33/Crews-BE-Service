package org.crews.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.AccountOneResponse;
import org.crews.service.AgitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/agits")
public class AgitController {
    private final AgitService agitService;

    @GetMapping("/{agits-id}/accounts")
    public AccountOneResponse accountInfo(@PathVariable("agits-id") Long agitId){
        return agitService.accountInfo(agitId);
    }
}
