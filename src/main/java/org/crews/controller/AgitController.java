package org.crews.controller;

import lombok.RequiredArgsConstructor;
import org.crews.dto.response.AgitResponse;
import org.crews.dto.AgitRequest;
import org.crews.service.AgitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agits")
public class AgitController {
    private final AgitService agitService;

    @GetMapping
    public ResponseEntity<List<AgitResponse>> getAllAgits(){
        return ResponseEntity.ok().body(agitService.getAllAgits());
    }

    @PostMapping
    public ResponseEntity<String> generateAgit(@RequestBody AgitRequest agitRequest){
        boolean isGenerated=agitService.generateAgit(agitRequest);
        if(isGenerated){
            return ResponseEntity.ok("등록 성공하였습니다.");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("등록 실패하였습니다.");
        }
    }
}
