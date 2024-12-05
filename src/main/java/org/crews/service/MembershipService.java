package org.crews.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String accountApprove(Long memberId, Long requestMemberId, Long agitId){
        checkExceptionUtil.validateLeader(memberId,agitId,AgitRole.LEADER);
        Membership membership = checkExceptionUtil.validateRole(requestMemberId, agitId, AgitRole.ADVANCED);
        membership.setAgitRole(AgitRole.STAFF);
        membershipRepository.save(membership);
        return "통장 권한이 부여되었습니다.";
    }

    @Transactional
    public String accountReject(Long memberId, Long requestMemberId, Long agitId){
        checkExceptionUtil.validateLeader(memberId,agitId,AgitRole.LEADER);
        Membership membership = checkExceptionUtil.validateRole(requestMemberId, agitId, AgitRole.ADVANCED);
        membership.setAgitRole(AgitRole.MEMBER);
        membershipRepository.save(membership);
        return "권한 요청이 거절되었습니다.";
    }

    @Transactional
    public String memberApprove(Long memberId, Long requestMemberId, Long agitId){
        checkExceptionUtil.validateLeader(memberId,agitId,AgitRole.LEADER);
        Membership membership = checkExceptionUtil.validateRole(requestMemberId, agitId, AgitRole.TEMP);
        membership.setAgitRole(AgitRole.MEMBER);
        membershipRepository.save(membership);
        return "가입신청이 승인되었습니다.";
    }

    @Transactional
    public String memberReject(Long memberId, Long requestMemberId, Long agitId){
        checkExceptionUtil.validateLeader(memberId,agitId,AgitRole.LEADER);
        Membership membership = checkExceptionUtil.validateRole(requestMemberId, agitId, AgitRole.TEMP);
        membershipRepository.delete(membership);
        return "가입신청이 거부되었습니다.";
    }
}
