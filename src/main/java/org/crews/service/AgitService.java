package org.crews.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.AgitInfoRequest;
import org.crews.dto.request.AgitRequest;
import org.crews.dto.request.DuesCallRequest;
import org.crews.dto.response.*;

import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.AgitRole;
import org.crews.repository.*;
import org.crews.utils.AESUtil;
import org.crews.utils.MessageUtil;
import org.crews.utils.AddressUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
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
    private final AddressRepository addressRepository;
    public AgitSliceResponse getAllAgits(Long subjectId, int page, Optional<Long> memberId){
        Long memberIdOptional = memberId.orElse(null);
        if(page<0){
            throw new CustomException(ErrorCode.INVALID_PAGE_NUMBER);
        }
        if (subjectId != null && !subjectRepository.existsById(subjectId)) {
            throw new CustomException(ErrorCode.SUBJECT_NOT_FOUND);
        }

        Slice<Agit> agits=agitRepository.findAllBySubjectIdWithFetchJoin(memberIdOptional,subjectId, PageRequest.of(page,10, Sort.by(Sort.Order.desc("createdAt"))));

        return AgitSliceResponse.of(agits);
    }

    @Transactional
    public AgitResponse generateAgit(AgitRequest agitRequest, Long memberId) {
        String addressDo = agitRequest.getAddressRequest().getDoName();
        String addressSi = agitRequest.getAddressRequest().getSiName();
        String addressGuGun = agitRequest.getAddressRequest().getGuName();
        String addressDong = agitRequest.getAddressRequest().getDongName();

        String uniqueKey = AddressUtils.generateUniqueAddressKey(addressDo, addressSi, addressGuGun, addressDong);
        Address findOrSaveAddress = addressRepository.findByUniqueAddressKey(uniqueKey)
                .orElseGet(() -> {
                    // 주소가 없으면 새로 생성하여 저장
                    Address address = Address.builder()
                            .addressDo(addressDo)
                            .addressSi(addressSi)
                            .addressGuGun(addressGuGun)
                            .addressDong(addressDong)
                            .build();
                    return addressRepository.save(address);
                });
        Subject subject = subjectRepository.findById(agitRequest.getSubject()).orElseThrow(
                () -> new CustomException(ErrorCode.SUBJECT_NOT_FOUND)
        );
        Agit agit = Agit.builder().agitName(agitRequest.getName()).isDue(false)
                .maxPerson(30).currentPerson(1).isDeleted(false).subject(subject).introduction(agitRequest.getIntroduction()).address(findOrSaveAddress)
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
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Membership membership = Membership.builder().agit(savedAgit).member(member).agitRole(AgitRole.LEADER).joinedAt(
                LocalDateTime.now()).build();
        memberShipRepository.save(membership);
        return AgitResponse.from(savedAgit);

    }

    public DuesAlarmResponse getDuesAlarm(Long agitId, Long memberId, Integer year, Integer month) {
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
                        content -> content.getStandardDate().getMonthValue() == month
                                && (content.getStandardDate().getYear() == year))
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

    public AgitRole getMemberRole(Long agitId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND)
        );
        return membership.getAgitRole();
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
    public ResponseEntity<AgitRegisterResponse> agitRestration(AgitInfoRequest agitInfoRequest, Long memberId) {
        try {
            Membership membership = Membership.builder()
                    .agit(Agit.builder().id(agitInfoRequest.getAgitId()).build())
                    .member(Member.builder().id(memberId).build())
                    .agitRole(AgitRole.TEMP)
                    .joinedAt(LocalDateTime.now())
                    .build();

            boolean isAlreadyJoined = memberShipRepository.findByMemberAndAgit(
                    Member.builder().id(memberId).build(),
                    Agit.builder().id(agitInfoRequest.getAgitId()).build()
            ).isPresent();

            if (isAlreadyJoined) {
                throw new CustomException(ErrorCode.AGIT_ALREADY_JOINED);
            }

            memberShipRepository.save(membership);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new AgitRegisterResponse("가입 신청이 완료되었습니다."));
        } catch (Exception e) {
            log.error("Error during agit registration: {}", e.getMessage(), e);
            throw new CustomException(ErrorCode.AGIT_APPLY_ERROR);
        }
    }

    public AgitSliceResponse searchAgit(String keyWord, Long memberId, Pageable pageable) {
        Slice<Agit> agitSlice = agitRepository.findByKeywordAndNotJoined(keyWord, memberId, pageable);


        return AgitSliceResponse.of(agitSlice);
    }

    public AgitSliceResponse searchAgitAll(String keyWord, Pageable pageable) {
        Slice<Agit> agitSlice = agitRepository.findByIntroductionLikeAndIsDeletedFalse(keyWord, pageable);

        return AgitSliceResponse.of(agitSlice);
    }

    public AgitRole getAgitRole(Long agitId, Long memberId) {
        Membership membership = memberShipRepository.findByMemberAndAgit(Member.builder().id(memberId).build(), Agit.builder().id(agitId).build()).orElseThrow();
        return membership.getAgitRole();
    }

    public AgitManageResponse getAgitMember(Long agitId, AgitRole agitRole) {
        List<Membership> membershipList = memberShipRepository.findTop3ByAgitAndAgitRoleNot(Agit.builder().id(agitId).build(), AgitRole.TEMP);
        Long totalMembership = memberShipRepository.countByAgitAndAgitRoleNot(Agit.builder().id(agitId).build(), AgitRole.TEMP);

        List<Membership> tempMembershipList = memberShipRepository.findTop3ByAgitAndAgitRoleLike(Agit.builder().id(agitId).build(), AgitRole.TEMP);
        Long totalTempMembership = memberShipRepository.countByAgitAndAgitRoleLike(Agit.builder().id(agitId).build(), AgitRole.TEMP);

        Iterator<Membership> membershipIterator = membershipList.iterator();
        Iterator<Membership> tempMembershipIterator = tempMembershipList.iterator();
        List<AgitManageMemberResponse> memberResponses = new ArrayList<>();
        List<AgitManageMemberResponse> tempMemberResponses = new ArrayList<>();

        while(membershipIterator.hasNext()) {
            Membership membership = membershipIterator.next();
            memberResponses.add(AgitManageMemberResponse.from(membership.getMember(), membership.getAgitRole()));
        }

        if(agitRole.equals(AgitRole.LEADER)) {
            while(tempMembershipIterator.hasNext()) {
                Membership membership = tempMembershipIterator.next();
                tempMemberResponses.add(AgitManageMemberResponse.from(membership.getMember(), membership.getAgitRole()));
            }
        }


        return AgitManageResponse.builder()
                .members(memberResponses)
                .requestedMembers(tempMemberResponses)
                .currentMember(totalMembership)
                .requestedMember(totalTempMembership)
                .message("")
                .build();
    }

    public AgitSortResponse getHomeAgits(Optional<Long> memberId){
        Long memberIdOptional = memberId.orElse(null);

        List<Agit> newAgitList = agitRepository.findNewAgits(memberIdOptional);
        List<AgitResponse> newAgitResponses = newAgitList.stream()
                .map(AgitResponse::from).limit(3).toList();

        List<Agit> recruitAgitList = agitRepository.findRecruitAgits(memberIdOptional);
        List<AgitResponse> recruitAgitResponses = recruitAgitList.stream()
                .map(AgitResponse::from).limit(3).toList();

        return new AgitSortResponse(recruitAgitResponses, newAgitResponses);
    }

    public void duesCall(Long agitId, Long memberId, DuesCallRequest duesCallRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        Membership membership = memberShipRepository.findByAgitAndAgitRole(agit, AgitRole.LEADER).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_ACCOUNT_NOT_FOUND)
        );
        String ci = membership.getMember().getCi();
        if (!member.getCi().equals(ci)) {
            throw new CustomException(ErrorCode.AUTHORIZED_ACCOUNT_CREATION);
        }
        List<Member> memberList = memberRepository.findByIdIn(duesCallRequest.getMemberId());
        DecimalFormat numberFormat = new DecimalFormat("#");
        DecimalFormat amountFormat = new DecimalFormat("#,###");
        String message = "안녕하세요 크루즈 입니다.\\n{0} 아지트에서 {1}년 {2}월 회비 {3}원을 아직 납부하지 않았습니다.\\n모임장 님께서 아지트 회비 납부 요청을 하셨습니다.\\n감사합니다.";
        String result = MessageFormat.format(
                message,
                agit.getAgitName(),
                numberFormat.format(duesCallRequest.getYear()),
                duesCallRequest.getMonth(),
                amountFormat.format(duesCallRequest.getDuesAmount())
        );
        for (Member findMember : memberList) {
            MessageUtil.send(AESUtil.decrypt(findMember.getPhoneNumber()), result);
        }
    }


    public AgitNameValidateResponse validateAgitName(String agitName) {

        if(agitRepository.existsByAgitName(agitName)) {
            return AgitNameValidateResponse.builder().used(true).message("이미 사용중인 아지트 이름입니다.").build();
        } else {
            return AgitNameValidateResponse.builder().used(false).message("사용 가능한 아지트 이름입니다.").build();
        }
    }
}
