package org.crews.controller;

import lombok.RequiredArgsConstructor;
import org.crews.dto.response.AgitResponse;
import org.crews.dto.request.AgitRequest;
import org.crews.model.Agit;
import org.crews.service.AgitService;
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
    public ResponseEntity<Agit> generateAgit(@RequestBody AgitRequest agitRequest){
        return ResponseEntity.ok().body(agitService.generateAgit(agitRequest));
    }
}
