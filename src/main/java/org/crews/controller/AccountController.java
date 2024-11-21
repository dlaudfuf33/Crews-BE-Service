package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.AccountInfoResponse;
import org.crews.dto.request.AccountDetailsResponse;
import org.crews.dto.request.AccountLinkRequest;
import org.crews.dto.request.MemberIdDto;
import org.crews.dto.core.AccountIssuedResponse;
import org.crews.dto.core.AccountOneResponse;
import org.crews.dto.response.AccountLinkResponse;
import org.crews.dto.response.TransactionDetailResponse;
import org.crews.model.AgitAndAccount;
import org.crews.model.constants.MemberRole;
import org.crews.service.AccountService;
import org.crews.utils.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/agits/{agits-id}/accounts")
public class AccountController {
    private final AccountService accountService;
    private final AuthUtil authUtil;

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
    public ResponseEntity<AccountLinkResponse> accountLink(@PathVariable("agits-id") Long agitId,
                                                           @RequestBody AccountLinkRequest accountLinkRequest){
        return ResponseEntity.ok().body(accountService.accountLink(agitId, accountLinkRequest));
    }

    @PostMapping("/all")
    public ResponseEntity<AccountInfoResponse> getAllAccounts(@PathVariable("agits-id") Long agitId,
                                                              @RequestBody MemberIdDto memberIdDto){
        return ResponseEntity.ok().body(accountService.getAllAccounts(agitId, memberIdDto));
    }

    @PostMapping("/{accounts-id}/details")
    public ResponseEntity<TransactionDetailResponse> accountDetails(@PathVariable("agits-id") Long agitId,
                                                                    @PathVariable("accounts-id") Long accountId,
                                                                    @RequestBody AccountDetailsResponse accountDetailsResponse,
                                                                    HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(accountService.accountDetails(agitId, accountId,memberId, accountDetailsResponse));
    }
}