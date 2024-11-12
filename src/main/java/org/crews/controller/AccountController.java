package org.crews.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.AccountLinkRequest;
import org.crews.dto.MemberIdDto;
import org.crews.dto.core.AccountIssuedResponse;
import org.crews.dto.core.AccountOneResponse;
import org.crews.model.AgitAndAccount;
import org.crews.service.AccountService;
import org.crews.service.AgitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/agits/{agits-id}/accounts")
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<AccountOneResponse> accountInfo(@PathVariable("agits-id") Long agitId){
        return ResponseEntity.ok().body(accountService.accountInfo(agitId));
    }

    @PostMapping
    public ResponseEntity<AccountIssuedResponse> accountIssued(@PathVariable("agits-id") Long agitId,
                                                               @RequestBody MemberIdDto memberIdDto){
        return ResponseEntity.ok().body(accountService.accountIssued(agitId, memberIdDto));
    }

    @PostMapping("/link")
    public ResponseEntity<AgitAndAccount> accountLink(@PathVariable("agits-id") Long agitId,
                                                      @RequestBody AccountLinkRequest accountLinkRequest){
        return ResponseEntity.ok().body(accountService.accountLink(agitId, accountLinkRequest));
    }
}
