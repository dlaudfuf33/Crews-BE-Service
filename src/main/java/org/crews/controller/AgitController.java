package org.crews.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.MemberIdDto;
import org.crews.dto.core.AccountIssuedResponse;
import org.crews.dto.core.AccountOneResponse;
import org.crews.model.Account;
import org.crews.service.AgitService;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{agits-id}/accounts")
    public AccountIssuedResponse accountIssued(@PathVariable("agits-id") Long agitId,
                                               @RequestBody MemberIdDto memberIdDto){
        return agitService.accountIssued(agitId, memberIdDto);
    }
}
