package org.crews.controller;

import lombok.RequiredArgsConstructor;
import org.crews.dto.response.InterestingResponseDto;
import org.crews.service.InterestingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/interests")
public class InterestsController {
    private final InterestingService interestService;

    @GetMapping
    public ResponseEntity<List<InterestingResponseDto>> getAllInterests(){
        return ResponseEntity.ok(interestService.getAllInterestings());
    }
}
