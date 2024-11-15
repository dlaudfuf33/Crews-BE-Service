package org.crews.controller;

import lombok.RequiredArgsConstructor;
import org.crews.dto.response.AgitResponse;
import org.crews.repository.AgitRepository;
import org.crews.service.AgitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agits")
public class AgitController {
    private final AgitService agitService;
    private final AgitRepository agitRepository;

    @GetMapping
    public List<AgitResponse> GetAllAgits(){
        return agitService.getAllAgits();
    }


}
