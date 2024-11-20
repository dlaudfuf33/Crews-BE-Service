package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.MeetingRequest;
import org.crews.dto.response.MeetingResponse;
import org.crews.dto.response.MeetingSliceResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.Agit;
import org.crews.model.Meeting;
import org.crews.model.Member;
import org.crews.model.Membership;
import org.crews.model.constants.MemberRole;
import org.crews.repository.AgitRepository;
import org.crews.repository.MeetingRepository;
import org.crews.repository.MemberRepository;
import org.crews.repository.MemberShipRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final AgitRepository agitRepository;
    private final MemberRepository memberRepository;
    private final MemberShipRepository memberShipRepository;

    public MeetingSliceResponse getAllEvents(Long memberId, Long agitId, int page) {
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));

        Slice<Meeting> events = meetingRepository.findByAgitIdAndIsDeletedFalse(agitId, PageRequest.of(page, 10, Sort.by(Sort.Order.desc("regularTime"))));
        return MeetingSliceResponse.of(membership, events);
    }

    public MeetingResponse getEvent(Long memberId, Long agitId, Long meetingId) {
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));

        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(
                () -> new CustomException(ErrorCode.EVENT_NOT_FOUND));

        if(meeting.isDeleted()) throw new CustomException(ErrorCode.DELETED_MEETING);

        return MeetingResponse.from(meeting);
    }

    @Transactional
    public MeetingResponse postEvent(Long memberId, Long agitId, MeetingRequest meetingRequest) {
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));

        if(!membership.getRole().equals(MemberRole.LEADER)){
            throw new CustomException(ErrorCode.AUTHORIZED_MEETING_CREATION);
        }

        Meeting meeting = Meeting.of(meetingRequest, agit);
        return MeetingResponse.from(meetingRepository.save(meeting));
    }
}
