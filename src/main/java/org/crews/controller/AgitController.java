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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/search")
    public ResponseEntity<AgitSliceResponse> searchAgits(@RequestParam String keyWord, @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, HttpServletRequest request){
        if(request.getHeader("Authorization") == null) {
            log.info("여기가 찍히나?");
            return ResponseEntity.status(HttpStatus.OK).body(agitService.searchAgitAll("%" + keyWord + "%", pageable));
        }

        Long memberId = authUtil.getMemberId(request);
        log.info("여기 찍히나?");
        log.info(String.valueOf(memberId));

        return ResponseEntity.status(HttpStatus.OK).body(agitService.searchAgit("%" + keyWord + "%", memberId, pageable));
    }
}
