package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.EventRequest;
import org.crews.dto.response.EventResponse;
import org.crews.dto.response.EventSliceResponse;
import org.crews.excaption.CustomException;
import org.crews.excaption.ErrorCode;
import org.crews.model.Agit;
import org.crews.model.Event;
import org.crews.model.Member;
import org.crews.repository.AgitRepository;
import org.crews.repository.EventRepository;
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
public class EventService {

    private final EventRepository eventRepository;
    private final AgitRepository agitRepository;
    private final MemberRepository memberRepository;
    private final MemberShipRepository memberShipRepository;

    public EventSliceResponse getAllEvents(Long memberId, Long agitId, int page) {
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));

        Slice<Event> events = eventRepository.findByAgitId(agitId, PageRequest.of(page, 10, Sort.by(Sort.Order.desc("regularTime"))));
        return EventSliceResponse.from(events);
    }

    public EventResponse getEvent(Long memberId, Long agitId, Long eventId) {
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new CustomException(ErrorCode.EVENT_NOT_FOUND));

        return EventResponse.from(event);
    }

    @Transactional
    public EventResponse postEvent(Long memberId, Long agitId, EventRequest eventRequest) {
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));

        Event event = Event.of(eventRequest, agit);
        return EventResponse.from(eventRepository.save(event));
    }
}
