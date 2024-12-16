package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.AgitAuthorRequest;
import org.crews.dto.request.DuesCallRequest;
import org.crews.dto.response.*;
import org.crews.dto.request.AgitRequest;
import org.crews.service.AgitService;
import org.crews.service.MembershipService;
import org.crews.utils.AuthUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.crews.dto.request.AgitInfoRequest;
import org.crews.model.constants.AgitRole;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/agits")
public class AgitController {
    private final AgitService agitService;
    private final MembershipService membershipService;
    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<AgitSliceResponse> getAllAgits(
            @RequestParam(value="subject-id",required = false)Long subjectId,
            @RequestParam int page, HttpServletRequest request){
        Optional<Long> memberId = Optional.ofNullable(authUtil.getMemberId(request));
        return ResponseEntity.ok().body(agitService.getAllAgits(subjectId,page,memberId));
    }

    @PostMapping
    public ResponseEntity<AgitResponse> generateAgit(@RequestBody AgitRequest agitRequest, HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(agitService.generateAgit(agitRequest, memberId));
    }

    @GetMapping("/{agits-id}/dues")
    public ResponseEntity<DuesAlarmResponse> getDuesAlarm(@PathVariable("agits-id") Long agitId,
                                                          @RequestParam Integer year,
                                                          @RequestParam Integer month,
                                                          HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(agitService.getDuesAlarm(agitId, memberId, year, month));
    }

    @GetMapping("/{agits-id}/role")
    public ResponseEntity<AgitRole> getMemberRole(@PathVariable("agits-id") Long agitId,
                                                  HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(agitService.getAgitRole(agitId, memberId));
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
    public ResponseEntity<AgitSliceResponse> searchAgits(@RequestParam String keyWord, @PageableDefault( size = 5, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, HttpServletRequest request){
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

    @GetMapping("/{agits-id}/manage/details")
    public ResponseEntity<AgitManageResponse> accountManage(@PathVariable("agits-id") Long agitId, HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        AgitRole agitRole = agitService.getAgitRole(agitId, memberId);
        if(agitRole.equals(AgitRole.TEMP)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(AgitManageResponse.builder().message("접근 권한이 없습니다.").build());
        }
        AgitManageResponse agitMemberResponse = agitService.getMembers(agitId);

        return ResponseEntity.status(HttpStatus.OK).body(agitMemberResponse);
    }
    @GetMapping("/{agits-id}/manage/approve")
    public ResponseEntity<AgitManageResponse> memberManage(@PathVariable("agits-id") Long agitId, HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        AgitRole agitRole = agitService.getAgitRole(agitId, memberId);
        if(agitRole.equals(AgitRole.TEMP)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(AgitManageResponse.builder().message("접근 권한이 없습니다.").build());
        }
        AgitManageResponse agitTempResponse = agitService.getTemps(agitId);
        return ResponseEntity.status(HttpStatus.OK).body(agitTempResponse);
    }
    @PostMapping("/{agits-id}/manage/details")
    public ResponseEntity<MembershipResponse> accountAuthorization(
            @PathVariable("agits-id") Long agitId, @RequestBody AgitAuthorRequest agitAuthorRequest, HttpServletRequest request
    ){
        Long memberId=authUtil.getMemberId(request);
        MembershipResponse membershipResponse = membershipService.accountAuthor(memberId, agitId, agitAuthorRequest);
        return ResponseEntity.ok().body(membershipResponse);
    }

    @PostMapping("/{agits-id}/manage/approve")
    public ResponseEntity<MembershipResponse> memberAuthorization(
            @PathVariable("agits-id") Long agitId,
            @RequestBody AgitAuthorRequest agitAuthorRequest, HttpServletRequest request
    ){
        Long memberId = authUtil.getMemberId(request);
        MembershipResponse membershipResponse = membershipService.memberAuthor(memberId, agitId, agitAuthorRequest);
        return ResponseEntity.ok().body(membershipResponse);
    }

    @GetMapping("/home")
    public ResponseEntity<AgitSortResponse> getHomeAgits(HttpServletRequest request){
        Optional<Long> memberId = Optional.ofNullable(authUtil.getMemberId(request));
        return ResponseEntity.ok().body(agitService.getHomeAgits(memberId));
    }

    @PostMapping("/{agits-id}/member/call")
    public ResponseEntity<String> duesCall(@PathVariable("agits-id") Long agitId,
                                           @RequestBody DuesCallRequest duesCallRequest,
                                         HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        agitService.duesCall(agitId, memberId, duesCallRequest);
        return ResponseEntity.ok().body("인증번호가 발송되었습니다!");

    }
    @GetMapping("/validate-name")
    public ResponseEntity<AgitNameValidateResponse> validateName(@RequestParam String agitName, HttpServletRequest request){
        return ResponseEntity.ok().body(agitService.validateAgitName(agitName));
    }


    @PostMapping("/{agits-id}/dues-call")
    public ResponseEntity<String> agitDuesCall(@PathVariable("agits-id") Long agitId,
                                               @RequestBody DuesCallRequest duesCallRequest,
                                               HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
         agitService.agitDuesCall(agitId, memberId, duesCallRequest);

        return ResponseEntity.status(HttpStatus.OK).body("문자가 정상적으로 발송되었습니다.");
    }

    @GetMapping("/{agits-id}/products")
    public ResponseEntity<ProductAllResponse> getAllProducts(@PathVariable("agits-id") Long agitId, HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(agitService.getAllProducts(memberId, agitId));
    }
}
