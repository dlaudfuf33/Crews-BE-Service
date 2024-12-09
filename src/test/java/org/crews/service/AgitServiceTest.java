package org.crews.service;

import org.crews.dto.request.AddressRequest;
import org.crews.dto.request.AgitRequest;
import org.crews.dto.response.AgitResponse;
import org.crews.model.*;
import org.crews.repository.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest(properties = {"CORE_API_CORE_URL=http://test.api.mock/core"})
class AgitServiceTest {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceTest.class);
    @Autowired
    private AgitService agitService;

    @MockBean
    private AgitRepository agitRepository;

    @MockBean
    private InterestingRepository interestingRepository;

    @MockBean
    private InterestingAndAgitRepository interestingAndAgitRepository;

    @MockBean
    private MemberShipRepository memberShipRepository;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private SubjectRepository subjectRepository;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private CoreService coreService;

    @MockBean
    private IntroducingRepository introducingRepository;


    @Test
    void generateAgit_성공() {
        // given
        Long memberId = 1L;

        AddressRequest addressRequest = new AddressRequest("서울특별시", "강남구", "삼성동", "테헤란로");
        AgitRequest agitRequest = new AgitRequest();
        agitRequest.setAddressRequest(addressRequest);
        agitRequest.setInterests(List.of(2L, 3L));
        agitRequest.setName("스터디 모임");
        agitRequest.setSubject(2L);
        agitRequest.setIntroduction("스터디 모임 소개");

        Member member = new Member();
        member.setId(memberId);

        Subject subject = new Subject();
        subject.setId(2L);
        subject.setSubjectName("자기계발/공부");

        Address address = new Address();
        address.setId(1L);
        address.setAddressDo("서울특별시");
        address.setAddressSi("강남구");
        address.setAddressGuGun("삼성동");
        address.setAddressDong("테헤란로");


        Agit agit = new Agit();
        agit.setId(1L);
        agit.setAgitName("스터디 모임");

        Interesting interesting1 = new Interesting();
        interesting1.setId(2L);
        interesting1.setName("외국어 공부");

        Interesting interesting2 = new Interesting();
        interesting2.setId(3L);
        interesting2.setName("독서 모임");

        Introducing introducing = new Introducing();
        introducing.setAgit(agit);
        introducing.setIntroduce("스터디 모임 소개");
        introducing.setImage("");
        introducing.setContent("");


        // Mock 설정
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(subjectRepository.findById(2L)).thenReturn(Optional.of(subject));
        when(addressRepository.findByUniqueAddressKey(any())).thenReturn(Optional.of(address));
        when(agitRepository.save(any())).thenReturn(agit);
        when(interestingRepository.findById(2L)).thenReturn(Optional.of(interesting1));
        when(interestingRepository.findById(3L)).thenReturn(Optional.of(interesting2));
        when(introducingRepository.save(any())).thenReturn(introducing); // Mock introducing 저장


        // when
        AgitResponse response = agitService.generateAgit(agitRequest, memberId);

        // then
        assertNotNull(response);
        assertEquals("스터디 모임", response.getName());
        verify(agitRepository, times(1)).save(any(Agit.class));
        log.info("생성된 모임: {}", response);
    }

}

