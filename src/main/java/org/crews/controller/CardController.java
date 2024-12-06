package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.AccountLinkRequest;
import org.crews.dto.core.CardIssuedResponse;
import org.crews.dto.request.CardRemoveRequest;
import org.crews.dto.core.MessageResponse;
import org.crews.dto.response.CardIssuanceResponse;
import org.crews.service.CardService;
import org.crews.utils.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/agits/{agits-id}/accounts/cards")
public class CardController {
    private final CardService cardService;
    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<CardIssuanceResponse> cardIssuance(@PathVariable("agits-id")Long agitId,
                                                             HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(cardService.cardIssuance(agitId, memberId));
    }

    @PostMapping
    public ResponseEntity<CardIssuedResponse> cardIssued(@PathVariable("agits-id")Long agitId,
                                                        HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(cardService.cardIssued(agitId, memberId));
    }

    @DeleteMapping
    public ResponseEntity<MessageResponse> cardRemove(@PathVariable("agits-id")Long agitId,
                                                      @RequestBody CardRemoveRequest cardReissuedRequest,
                                                      HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(cardService.cardRemove(agitId, memberId, cardReissuedRequest));
    }
}
