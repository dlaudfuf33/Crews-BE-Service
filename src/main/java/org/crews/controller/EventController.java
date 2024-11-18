package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.MemberDetails;
import org.crews.dto.response.EventResponse;
import org.crews.jwt.JWTUtil;
import org.crews.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/agits/{agits-id}/events")
public class EventController {

    private final EventService eventService;
    private final JWTUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents(@PathVariable("agits-id") Long agitsId, HttpServletRequest request) {
        Long memberId = jwtUtil.getMemberId(request.getHeader("Authorization").substring(7));

        return ResponseEntity.ok().body(eventService.getAllEvents(agitsId, memberId));
    }
}
