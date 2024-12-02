package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.AccountInfoResponse;
import org.crews.dto.core.ApiResponse;
import org.crews.dto.request.AccountDetailsRequest;
import org.crews.dto.request.AccountLinkRequest;
import org.crews.dto.core.AccountIssuedResponse;
import org.crews.dto.core.AccountOneResponse;
import org.crews.dto.request.AccountTransferRequest;
import org.crews.dto.response.AccountLinkResponse;
import org.crews.dto.response.TransactionDetailResponse;

import org.crews.dto.request.MemberIdRequest;
import org.crews.model.constants.AgitRole;
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
                                                               @RequestBody MemberIdRequest memberIdRequest){
        return ResponseEntity.ok().body(accountService.accountIssued(agitId, memberIdRequest, AgitRole.LEADER));
    }

    @PostMapping("/link")
    public ResponseEntity<AccountLinkResponse> accountLink(@PathVariable("agits-id") Long agitId,
                                                           @RequestBody AccountLinkRequest accountLinkRequest){
        return ResponseEntity.ok().body(accountService.accountLink(agitId, accountLinkRequest));
    }

    @PostMapping("/all")
    public ResponseEntity<AccountInfoResponse> getAllAccounts(@PathVariable("agits-id") Long agitId,
                                                              @RequestBody MemberIdRequest memberIdRequest){
        return ResponseEntity.ok().body(accountService.getAllAccounts(agitId, memberIdRequest));
    }

    @GetMapping("/details")
    public ResponseEntity<TransactionDetailResponse> accountDetails(@PathVariable("agits-id") Long agitId,
                                                                    @RequestParam Integer selectPeriod,
                                                                    @RequestParam String transactionType,
                                                                    @RequestParam String order,
                                                                    HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        AccountDetailsRequest accountDetailsRequest = AccountDetailsRequest.builder().selectPeriod(selectPeriod).transactionType(transactionType).order(order).build();
        return ResponseEntity.ok().body(accountService.accountDetails(agitId, memberId, accountDetailsRequest));
    }

    @GetMapping("/deposit")
    public ResponseEntity<TransactionDetailResponse> getAccountDeposit(@PathVariable("agits-id") Long agitId,
                                                             @RequestParam Integer year,
                                                             @RequestParam Integer month,
                                                             HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(accountService.getAccountDeposit(agitId, memberId, year, month));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse> transferCrewAccount(@PathVariable("agits-id") Long agitId,
                                                           @RequestBody AccountTransferRequest accountTransferRequest,
                                                           HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(accountService.transferCrewAccount(agitId, memberId, accountTransferRequest));
    }

}
