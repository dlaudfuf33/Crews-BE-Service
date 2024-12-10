package org.crews.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.MeetingRequest;
import org.crews.dto.response.MeetingResponse;
import org.crews.dto.response.MeetingSliceResponse;
import org.crews.service.MeetingService;
import org.crews.utils.AuthUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/agits/{agits-id}/meetings")
public class MeetingController {

    private final MeetingService meetingService;
    private final AuthUtil authUtil;

    @GetMapping
    public ResponseEntity<MeetingSliceResponse> getAllMeetings(
            @PathVariable("agits-id") Long agitId,
            @RequestParam Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(meetingService.getAllEvents(memberId, agitId, page, pageSize));
    }

    @GetMapping("/{meeting-id}")
    public ResponseEntity<MeetingResponse> getMeeting(
            @PathVariable("agits-id") Long agitId,
            @PathVariable("meeting-id") Long meetingId,
            HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);

        return ResponseEntity.ok().body(meetingService.getEvent(memberId, agitId,meetingId));
    }

    @GetMapping("/{meeting-id}/edit")
    public ResponseEntity<MeetingResponse> getMeetingForEdit(
            @PathVariable("agits-id") Long agitId,
            @PathVariable("meeting-id") Long meetingId,
            HttpServletRequest request) {
        Long memberId = authUtil.getMemberId(request);

        return ResponseEntity.ok().body(meetingService.getEvent(memberId, agitId,meetingId));
    }

    @GetMapping("/recent")
    public ResponseEntity<Integer> getMeetingsForMonth(
            @PathVariable("agits-id") Long agitId){
        return ResponseEntity.ok().body(meetingService.getMeetingsForMonth(agitId));
    }

    @PostMapping("/create")
    public ResponseEntity<MeetingResponse> createMeeting(
            @PathVariable("agits-id") Long agitId,
            @RequestBody MeetingRequest meetingRequest,
            HttpServletRequest request){
        Long memberId = authUtil.getMemberId(request);

        return ResponseEntity.ok().body(meetingService.postEvent(memberId, agitId, meetingRequest));
    }

    @PatchMapping("/{meeting-id}/edit")
    public ResponseEntity<MeetingResponse> updateMeeting(
            @PathVariable("agits-id") Long agitId,
            @PathVariable("meeting-id") Long meetingId,
            @RequestBody @Valid  MeetingRequest meetingRequest,
            HttpServletRequest request
    ){
        Long memberId = authUtil.getMemberId(request);
        return ResponseEntity.ok().body(meetingService.editEvent(memberId, agitId, meetingId, meetingRequest));
    }

}
