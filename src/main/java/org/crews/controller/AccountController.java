package org.crews.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.AccountInfoResponse;
import org.crews.dto.request.AccountLinkRequest;
import org.crews.dto.request.MemberIdDto;
import org.crews.dto.core.AccountIssuedResponse;
import org.crews.dto.core.AccountOneResponse;
import org.crews.model.AgitAndAccount;
import org.crews.model.constants.MemberRole;
import org.crews.service.AccountService;
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
        return ResponseEntity.ok().body(accountService.accountIssued(agitId, memberIdDto, MemberRole.LEADER));
    }

    @PostMapping("/link")
    public ResponseEntity<AgitAndAccount> accountLink(@PathVariable("agits-id") Long agitId,
                                                      @RequestBody AccountLinkRequest accountLinkRequest){
        return ResponseEntity.ok().body(accountService.accountLink(agitId, accountLinkRequest));
    }

    @PostMapping("/{accounts-id}/details")
    public ResponseEntity<AccountInfoResponse> accountDetails(@PathVariable("agits-id") Long agitId,
                                                              @PathVariable("accounts-id") Long accountId,
                                                              @RequestBody AccountLinkRequest accountLinkRequest){
        return ResponseEntity.ok().body(accountService.accountDetails(agitId, accountId, accountLinkRequest));
    }
}