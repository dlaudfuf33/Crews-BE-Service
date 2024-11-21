package org.crews.controller;

import lombok.RequiredArgsConstructor;
import org.crews.dto.response.SubjectsResponse;
import org.crews.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/interests")
public class InterestsController {
    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<SubjectsResponse> getAllInterests(){
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }
}
