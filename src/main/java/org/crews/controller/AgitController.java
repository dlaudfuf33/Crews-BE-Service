package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.AgitRegisterRequest;
import org.crews.dto.response.*;
import org.crews.dto.request.AgitRequest;
import org.crews.model.constants.MemberRole;
import org.crews.service.AgitService;
import org.crews.utils.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/agits")
public class AgitController {
    private final AgitService agitService;
    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<List<AgitResponse>> getAllAgits(){
        return ResponseEntity.ok().body(agitService.getAllAgits());
    }

    @PostMapping
    public ResponseEntity<AgitResponse> generateAgit(@RequestBody AgitRequest agitRequest){
        return ResponseEntity.ok().body(agitService.generateAgit(agitRequest));
    }

    @GetMapping("/{agits-id}/dues")
    public ResponseEntity<DuesAlarmResponse> getDuesAlarm(@PathVariable("agits-id") Long agitId,
                                                          HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(agitService.getDuesAlarm(agitId, memberId));
    }

    @GetMapping("/{agits-id}/role")
    public ResponseEntity<MemberRole> getMemberRole(@PathVariable("agits-id") Long agitId,
                                                    HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(agitService.getMemberRole(agitId, memberId));
    }

    @GetMapping("/info")
    public ResponseEntity<AllAgitsInfoResponse> getAgitsInfo(HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(agitService.getAgitsInfo(memberId));
    }

    @PostMapping("/registrations")
    public ResponseEntity<AgitRegisterResponse> registerAgit(@RequestBody AgitRegisterRequest agitRegisterRequest){
        return agitService.agitRestration(agitRegisterRequest);
    }

    @GetMapping("/home")
    public ResponseEntity<AgitSortResponse> getHomeAgits(HttpServletRequest request){
        Optional<Long> memberId = Optional.ofNullable(authUtil.getMemberId(request));
        return ResponseEntity.ok().body(agitService.getHomeAgits(memberId));
    }
}
