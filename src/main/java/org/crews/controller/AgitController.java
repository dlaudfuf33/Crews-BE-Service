package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.crews.dto.response.AgitResponse;
import org.crews.dto.request.AgitRequest;
import org.crews.dto.response.DuesAlarmResponse;
import org.crews.model.Agit;
import org.crews.service.AgitService;
import org.crews.utils.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
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

}
