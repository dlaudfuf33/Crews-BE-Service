package org.crews.controller;

import com.sun.jdi.request.EventRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.MemberDetails;
import org.crews.dto.response.EventResponse;
import org.crews.dto.response.EventSliceResponse;
import org.crews.jwt.JWTUtil;
import org.crews.service.EventService;
import org.springframework.data.domain.Slice;
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
    public ResponseEntity<EventSliceResponse> getAllEvents(
            @PathVariable("agits-id") Long agitId,
            @RequestParam int page,
            HttpServletRequest request) {
        Long memberId = jwtUtil.getMemberId(request.getHeader("Authorization").substring(7));

        return ResponseEntity.ok().body(eventService.getAllEvents(memberId, agitId, page));
    }

    @GetMapping("/{event-id}")
    public ResponseEntity<EventResponse> getEvent(
            @PathVariable("agits-id") Long agitId,
            @PathVariable("event-id") Long eventId,
            HttpServletRequest request) {
        Long memberId = jwtUtil.getMemberId(request.getHeader("Authorization").substring(7));

        return ResponseEntity.ok().body(eventService.getEvent(memberId, agitId,eventId));
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @PathVariable("agits-id") Long agitId,
            @RequestBody EventRequest eventRequest,
            HttpServletRequest request){
        Long memberId = jwtUtil.getMemberId(request.getHeader("Authorization").substring(7));

        return ResponseEntity.ok().body(eventService.postEvent(memberId, agitId, eventRequest));
    }
}
