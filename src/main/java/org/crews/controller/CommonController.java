package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.response.AccountHistoryFinalV2Response;
import org.crews.dto.response.AccountV2Response;
import org.crews.dto.response.ProductAllResponse;
import org.crews.service.CommonService;
import org.crews.service.CoreService;
import org.crews.utils.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommonController {
    private final CommonService commonService;
    private final CoreService coreService;
    private final AuthUtil authUtil;

    @GetMapping("/accounts")
    public ResponseEntity<AccountV2Response> getAccountV2(HttpServletRequest request,
                                                          @RequestParam Integer year,
                                                          @RequestParam Integer month){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(commonService.getAllAccounts(memberId, year, month));
    }

    @GetMapping("/accounts/history")
    public ResponseEntity<AccountHistoryFinalV2Response> getAccountHistoryV2(HttpServletRequest request,
                                                                             @RequestParam Integer year,
                                                                             @RequestParam Integer month){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(commonService.getMyAccountsHistory(memberId,year,month));
    }

    @GetMapping("/products")
    public ResponseEntity<ProductAllResponse> getAllProducts(){
        return ResponseEntity.ok().body(coreService.getAllProducts());
    }
}
