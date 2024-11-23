package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.DuesSaveRequest;
import org.crews.dto.response.DuesSaveResponse;
import org.crews.dto.response.GetDuesResponse;
import org.crews.jwt.JWTUtil;
import org.crews.service.DuesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/agits/{agits-id}/managements/dues")
public class DuesController {
    private final DuesService duesService;
    private final JWTUtil jwtUtil;

    @GetMapping
    public ResponseEntity<GetDuesResponse> getDues(@PathVariable("agits-id") Long agitId,
                                                   HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        Long memberId = jwtUtil.getMemberId(token);
        return ResponseEntity.ok().body(duesService.getDues(agitId, memberId));
    }

    @GetMapping("/common")
    public ResponseEntity<DuesSaveResponse> getDuesCommon(@PathVariable("agits-id") Long agitId,
                                                    HttpServletRequest request){
        String token = request.getHeader("Authorization").substring(7);
        Long memberId = jwtUtil.getMemberId(token);
        return ResponseEntity.ok().body(duesService.getDuesCommon(agitId, memberId));
    }


    @PostMapping("/common")
    public ResponseEntity<DuesSaveResponse> duesSaveCommon(@PathVariable("agits-id") Long agitId,
                                                     @RequestBody DuesSaveRequest duesSaveRequest){
        return ResponseEntity.ok().body(duesService.duesSaveCommon(agitId, duesSaveRequest));
    }
}
