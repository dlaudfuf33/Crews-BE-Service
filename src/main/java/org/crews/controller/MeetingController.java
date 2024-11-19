package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.MeetingRequest;
import org.crews.dto.response.MeetingResponse;
import org.crews.dto.response.MeetingSliceResponse;
import org.crews.jwt.JWTUtil;
import org.crews.service.MeetingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/agits/{agits-id}/meetings")
public class MeetingController {

    private final MeetingService meetingService;
    private final JWTUtil jwtUtil;

    @GetMapping
    public ResponseEntity<MeetingSliceResponse> getAllEvents(
            @PathVariable("agits-id") Long agitId,
            @RequestParam int page,
            HttpServletRequest request) {
        Long memberId = jwtUtil.getMemberId(request.getHeader("Authorization").substring(7));

        return ResponseEntity.ok().body(meetingService.getAllEvents(memberId, agitId, page));
    }

    @GetMapping("/{meeting-id}")
    public ResponseEntity<MeetingResponse> getEvent(
            @PathVariable("agits-id") Long agitId,
            @PathVariable("meeting-id") Long eventId,
            HttpServletRequest request) {
        Long memberId = jwtUtil.getMemberId(request.getHeader("Authorization").substring(7));

        return ResponseEntity.ok().body(meetingService.getEvent(memberId, agitId,eventId));
    }

    @PostMapping
    public ResponseEntity<MeetingResponse> createEvent(
            @PathVariable("agits-id") Long agitId,
            @RequestBody MeetingRequest meetingRequest,
            HttpServletRequest request){
        Long memberId = jwtUtil.getMemberId(request.getHeader("Authorization").substring(7));

        return ResponseEntity.ok().body(meetingService.postEvent(memberId, agitId, meetingRequest));
    }
}
