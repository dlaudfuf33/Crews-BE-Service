package org.crews.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.AccountLinkRequest;
import org.crews.dto.core.CardIssuedResponse;
import org.crews.dto.CardRemoveRequest;
import org.crews.dto.core.MessageResponse;
import org.crews.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/agits/{agits-id}/accounts/{accounts-id}/cards")
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<CardIssuedResponse> cardIssued(@PathVariable("agits-id")Long agitId,
                                                        @PathVariable("accounts-id") Long accountId,
                                                        @RequestBody AccountLinkRequest accountLinkRequest){
        return ResponseEntity.ok().body(cardService.cardIssued(agitId, accountId, accountLinkRequest));
    }

    @DeleteMapping
    public ResponseEntity<MessageResponse> cardRemove(@PathVariable("agits-id")Long agitId,
                                                      @PathVariable("accounts-id") Long accountId,
                                                      @RequestBody CardRemoveRequest cardReissuedRequest){
        return ResponseEntity.ok().body(cardService.cardRemove(agitId, accountId, cardReissuedRequest));
    }
}
