package org.crews.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.response.MembershipResponse;
import org.crews.model.Membership;
import org.crews.model.constants.AgitRole;
import org.crews.repository.MembershipRepository;
import org.crews.utils.CheckExceptionUtil;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MembershipService {
    private final MembershipRepository membershipRepository;
    private final CheckExceptionUtil checkExceptionUtil;

    @Transactional
    public MembershipResponse accountApprove(Long memberId, Long requestMemberId, Long agitId){
        checkExceptionUtil.validateLeader(memberId,agitId,AgitRole.LEADER);
        Membership membership = checkExceptionUtil.validateRole(requestMemberId, agitId, AgitRole.ADVANCED);
        membership.setAgitRole(AgitRole.STAFF);
        membershipRepository.save(membership);
        return MembershipResponse.builder()
                .message("통장 권한 요청을 승인하였습니다.")
                .build();
    }

    @Transactional
    public MembershipResponse accountReject(Long memberId, Long requestMemberId, Long agitId){
        checkExceptionUtil.validateLeader(memberId,agitId,AgitRole.LEADER);
        Membership membership = checkExceptionUtil.validateRole(requestMemberId, agitId, AgitRole.ADVANCED);
        membership.setAgitRole(AgitRole.MEMBER);
        membershipRepository.save(membership);
        return MembershipResponse.builder()
                .message("통장 권한 요청을 거부하였습니다.")
                .build();
    }

    @Transactional
    public MembershipResponse memberApprove(Long memberId, Long requestMemberId, Long agitId){
        checkExceptionUtil.validateLeader(memberId,agitId,AgitRole.LEADER);
        Membership membership = checkExceptionUtil.validateRole(requestMemberId, agitId, AgitRole.TEMP);
        membership.setAgitRole(AgitRole.MEMBER);
        membershipRepository.save(membership);
        return MembershipResponse.builder()
                .message("가입신청을 승인하였습니다.")
                .build();
    }

    @Transactional
    public MembershipResponse memberReject(Long memberId, Long requestMemberId, Long agitId){
        checkExceptionUtil.validateLeader(memberId,agitId,AgitRole.LEADER);
        Membership membership = checkExceptionUtil.validateRole(requestMemberId, agitId, AgitRole.TEMP);
        membershipRepository.delete(membership);
        return MembershipResponse.builder()
                .message("가입신청을 거부하였습니다.")
                .build();
    }
}
