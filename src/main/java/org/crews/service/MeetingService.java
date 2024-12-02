package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.MeetingRequest;
import org.crews.dto.response.AgitVaildationResponse;
import org.crews.dto.response.MeetingResponse;
import org.crews.dto.response.MeetingSliceResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.Agit;
import org.crews.model.Meeting;
import org.crews.model.constants.AgitRole;
import org.crews.repository.AgitRepository;
import org.crews.repository.MeetingRepository;
import org.crews.utils.CheckExceptionUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final CheckExceptionUtil checkExceptionUtil;
    private final AgitRepository agitRepository;

    public MeetingSliceResponse getAllEvents(Long memberId, Long agitId, int page) {
        AgitVaildationResponse checkedResult = checkExceptionUtil.checkAgitException(memberId, agitId);

        Slice<Meeting> events = meetingRepository.findByAgitIdAndIsDeletedFalse(agitId, PageRequest.of(page, 10, Sort.by(Sort.Order.desc("regularTime"))));
        return MeetingSliceResponse.of(checkedResult.getMembership(), events);
    }

    public MeetingResponse getEvent(Long memberId, Long agitId, Long meetingId) {
        AgitVaildationResponse checkedResult = checkExceptionUtil.checkAgitException(memberId, agitId);

        Meeting meeting = meetingRepository.findByIdAndAgit(meetingId, checkedResult.getAgit()).orElseThrow(
                () -> new CustomException(ErrorCode.EVENT_NOT_FOUND));
        if(meeting.isDeleted()) throw new CustomException(ErrorCode.DELETED_MEETING);

        return MeetingResponse.from(meeting);
    }

    public Integer getMeetingsForMonth(Long agitId){
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));
        LocalDateTime beforeMonth = LocalDateTime.now().minusMonths(1);
        List<Meeting> meetings = agit.getMeetings().stream()
                .filter(meeting -> meeting.getRegularTime().compareTo(beforeMonth) >= 0).toList();
        return meetings.size();
    }

    @Transactional
    public MeetingResponse postEvent(Long memberId, Long agitId, MeetingRequest meetingRequest) {
        AgitVaildationResponse checkedResult = checkExceptionUtil.checkAgitException(memberId, agitId);

        if(!checkedResult.getMembership().getAgitRole().equals(AgitRole.LEADER)){
            throw new CustomException(ErrorCode.AUTHORIZED_MEETING_CREATION);
        }

        Meeting meeting = Meeting.of(meetingRequest, checkedResult.getAgit());
        return MeetingResponse.from(meetingRepository.save(meeting));
    }

    @Transactional
    public MeetingResponse editEvent(Long memberId, Long agitId, Long meetingId, MeetingRequest meetingRequest) {
        AgitVaildationResponse checkedResult = checkExceptionUtil.checkAgitException(memberId, agitId);
        Meeting meeting=meetingRepository.findById(meetingId).orElseThrow(()->new CustomException(ErrorCode.EVENT_NOT_FOUND));
        if(!checkedResult.getMembership().getRole().equals(MemberRole.LEADER)){
            throw new CustomException(ErrorCode.AUTHORIZED_CAPTAIN_ONLY);
        }

        meeting.update(meetingRequest);
        return MeetingResponse.from(meetingRepository.save(meeting));


    }
}
