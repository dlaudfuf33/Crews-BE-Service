package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.response.EventResponse;
import org.crews.excaption.CustomException;
import org.crews.excaption.ErrorCode;
import org.crews.model.Agit;
import org.crews.model.Event;
import org.crews.model.Member;
import org.crews.model.Membership;
import org.crews.repository.AgitRepository;
import org.crews.repository.EventRepository;
import org.crews.repository.MemberRepository;
import org.crews.repository.MemberShipRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final AgitRepository agitRepository;
    private final MemberRepository memberRepository;
    private final MemberShipRepository memberShipRepository;

    public List<EventResponse> getAllEvents(Long agitId, Long memberId) {

        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                ()-> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));

        List<Event> events = agit.getEvents();
        return events.stream()
                .map(EventResponse::from)
                .collect(Collectors.toList());
    }
}
