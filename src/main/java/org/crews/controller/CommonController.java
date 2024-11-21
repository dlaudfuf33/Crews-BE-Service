package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.response.AccountV2Response;
import org.crews.service.CommonService;
import org.crews.utils.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommonController {
    private final CommonService commonService;
    private final AuthUtil authUtil;

    @GetMapping("/accounts")
    public ResponseEntity<AccountV2Response> AccountV2Response(HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(commonService.getAllAccounts(memberId));
    }
}
