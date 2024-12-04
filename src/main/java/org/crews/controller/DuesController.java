package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.DuesSaveRequest;
import org.crews.dto.response.DuesSaveResponse;
import org.crews.dto.response.GetDuesResponse;
import org.crews.service.DuesService;
import org.crews.utils.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/agits/{agits-id}/managements/dues")
public class DuesController {
    private final DuesService duesService;
    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<GetDuesResponse> getDues(@PathVariable("agits-id") Long agitId,
                                                   @RequestParam Integer year,
                                                   @RequestParam Integer month,
                                                   HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(duesService.getDues(agitId, memberId, year, month));
    }

    @GetMapping("/common")
    public ResponseEntity<DuesSaveResponse> getDuesCommon(@PathVariable("agits-id") Long agitId,
                                                    HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(duesService.getDuesCommon(agitId, memberId));
    }


    @PostMapping("/common")
    public ResponseEntity<DuesSaveResponse> duesSaveCommon(@PathVariable("agits-id") Long agitId,
                                                           @RequestBody DuesSaveRequest duesSaveRequest,
                                                           HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(duesService.duesSaveCommon(agitId, memberId, duesSaveRequest));
    }
}
