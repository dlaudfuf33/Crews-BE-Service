package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.response.*;
import org.crews.dto.request.AgitRequest;
import org.crews.service.AgitService;
import org.crews.utils.AuthUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.crews.dto.request.AgitInfoRequest;
import org.crews.model.constants.AgitRole;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<AgitRole> getMemberRole(@PathVariable("agits-id") Long agitId,
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
    public ResponseEntity<AgitRegisterResponse> registerAgit(@RequestBody AgitInfoRequest agitRegisterRequest, HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return agitService.agitRestration(agitRegisterRequest, memberId);
    }

    @GetMapping("/search")
    public ResponseEntity<AgitSliceResponse> searchAgits(@RequestParam String keyWord, @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, HttpServletRequest request){
        if(request.getHeader("Authorization") == null) {
            return ResponseEntity.status(HttpStatus.OK).body(agitService.searchAgitAll("%" + keyWord + "%", pageable));
        }

        Long memberId = authUtil.getMemberId(request);

        return ResponseEntity.status(HttpStatus.OK).body(agitService.searchAgit("%" + keyWord + "%", memberId, pageable));
    }

    @GetMapping("/{agits-id}/manage")
    public ResponseEntity<AgitManageResponse> agitManage(@PathVariable("agits-id") Long agitId, HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        AgitRole agitRole = agitService.getAgitRole(agitId, memberId);

        if(agitRole.equals(AgitRole.TEMP)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(AgitManageResponse.builder().message("접근 권한이 없습니다.").build());
        }

        AgitManageResponse agitmanageResponse = agitService.getAgitMember(agitId, agitRole);

        return ResponseEntity.status(HttpStatus.OK).body(agitmanageResponse);
    }

    @GetMapping("/home")
    public ResponseEntity<AgitSortResponse> getHomeAgits(HttpServletRequest request){
        Optional<Long> memberId = Optional.ofNullable(authUtil.getMemberId(request));
        return ResponseEntity.ok().body(agitService.getHomeAgits(memberId));
    }
}
