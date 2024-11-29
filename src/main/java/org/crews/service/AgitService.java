package org.crews.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.AgitRegisterRequest;
import org.crews.dto.request.AgitRequest;
import org.crews.dto.response.*;

import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.MemberRole;
import org.crews.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AgitService {
    private final AgitRepository agitRepository;
    private final InterestingRepository interestingRepository;
    private final InterestingAndAgitRepository interestingAndAgitRepository;
    private final IntroducingRepository introducingRepository;
    private final MemberShipRepository memberShipRepository;
    private final MemberRepository memberRepository;
    private final SubjectRepository subjectRepository;
    private final DuesRepository duesRepository;
    private final CommonDuesRepository commonDuesRepository;
    public List<AgitResponse> getAllAgits(){
        return agitRepository.findAllWithFetchJoin().stream().map(AgitResponse::from).toList();
    }
    @Transactional
    public AgitResponse generateAgit(AgitRequest agitRequest) {
        Subject subject = subjectRepository.findById(agitRequest.getSubject()).orElseThrow(
                () -> new CustomException(ErrorCode.SUBJECT_NOT_FOUND)
        );
        Agit agit = Agit.builder().agitName(agitRequest.getName()).isDue(false)
                .maxPerson(30).currentPerson(1).isDeleted(false).subject(subject).introduction(agitRequest.getIntroduction())
                .build();

        Agit savedAgit = agitRepository.save(agit);
        Introducing introducing = Introducing.builder().agit(savedAgit).introduce(savedAgit.getIntroduction()).image("").content("").build();
        introducingRepository.save(introducing);
        List<InterestingAndAgit> interestingAndAgits = new ArrayList<>();
        for (Long interest : agitRequest.getInterests()) {
            Interesting interesting = interestingRepository.findById(interest)
                    .orElseThrow(() -> new CustomException(ErrorCode.INTERESTS_NOT_FOUND));
            InterestingAndAgit interestingAndAgit = new InterestingAndAgit();
            interestingAndAgit.setAgit(savedAgit);
            interestingAndAgit.setInteresting(interesting);
            interestingAndAgits.add(interestingAndAgit);
        }
        savedAgit.getInterestingAndAgits().addAll(interestingAndAgits);
        interestingAndAgitRepository.saveAllAndFlush(interestingAndAgits);
        Member member = memberRepository.findById(agitRequest.getMemberId()).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Membership membership = Membership.builder().agit(savedAgit).member(member).role(MemberRole.LEADER).joinedAt(
                LocalDateTime.now()).build();
        memberShipRepository.save(membership);
        return AgitResponse.from(savedAgit);

    }

    public DuesAlarmResponse getDuesAlarm(Long agitId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND)
        );
        Optional<CommonDues> optionalCommonDues = commonDuesRepository.findByAgit(agit);
        if(optionalCommonDues.isEmpty()) return DuesAlarmResponse.builder().build();
        CommonDues commonDues = optionalCommonDues.get();
        List<Dues> duesList = duesRepository.findByMembershipAndCommonDues(membership, commonDues)
                .stream().filter(
                        content -> content.getDueDate().getMonth().equals(LocalDate.now().getMonth())
                                && (content.getDueDate().getYear() == LocalDate.now().getYear()))
                .toList();
        if(duesList.isEmpty()){
            return DuesAlarmResponse.builder().dueAmount(commonDues.getDueAmount()).dueDay(commonDues.getDueDay()).build();
        }else {
            BigDecimal amount = BigDecimal.ZERO;
            for(Dues dues : duesList){
                amount = amount.add(dues.getDueAmount());
            }
            if(amount.compareTo(commonDues.getDueAmount()) < 0){
                return DuesAlarmResponse.builder().dueAmount(commonDues.getDueAmount().subtract(amount)).dueDay(commonDues.getDueDay()).build();
            }
            else
                return DuesAlarmResponse.builder().dueAmount(BigDecimal.ZERO).dueDay(commonDues.getDueDay()).build();
        }
    }

    public MemberRole getMemberRole(Long agitId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND)
        );
        return membership.getRole();
    }

    public AllAgitsInfoResponse getAgitsInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        List<Membership> membershipList = memberShipRepository.findByMember(member);
        List<AgitInfoResponse> agitInfoResponseList = membershipList.stream().map(AgitInfoResponse::from).toList();
        return AllAgitsInfoResponse.builder().agitInfoList(agitInfoResponseList).build();
    }
    @Transactional
    public ResponseEntity<AgitRegisterResponse> agitRestration(AgitRegisterRequest agitRegisterRequest) {
        try {
            Membership membership = Membership.builder()
                    .agit(Agit.builder().id(agitRegisterRequest.getAgitId()).build())
                    .member(Member.builder().id(agitRegisterRequest.getMemberId()).build())
                    .role(MemberRole.TEMP)
                    .joinedAt(LocalDateTime.now())
                    .build();

            boolean isAlreadyJoined = memberShipRepository.findByMemberAndAgit(
                    Member.builder().id(agitRegisterRequest.getMemberId()).build(),
                    Agit.builder().id(agitRegisterRequest.getAgitId()).build()
            ).isPresent();

            if (isAlreadyJoined) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new AgitRegisterResponse("이미 가입한 아지트입니다."));
            }

            memberShipRepository.save(membership);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AgitRegisterResponse("가입 신청이 완료되었습니다."));
        } catch (Exception e) {
            log.error("Error during agit registration: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AgitRegisterResponse("아지트 가입신청을 하는 도중 문제가 발생하였습니다. 나중에 다시 시도해주세요."));
        }
    }

    public AgitSliceResponse searchAgit(String keyWord, Pageable pageable) {
        Slice<Agit> agitSlice = agitRepository.findByIntroductionLikeAndIsDeletedFalse(keyWord, pageable);

        return AgitSliceResponse.of(agitSlice);
    }
}
