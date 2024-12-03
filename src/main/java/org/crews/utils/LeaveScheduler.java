package org.crews.utils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.Member;
import org.crews.repository.MemberRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeaveScheduler {

    private final MemberRepository memberRepository;

    /**
     * 매일 자정에 실행되는 스케줄러
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void deleteSoftDeletedMembers() {
        log.info("소프트 삭제된 회원 중 30일 이상 지난 회원 삭제 작업 시작");
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(30);

        try {
            List<Member> membersToDelete = memberRepository.findSoftDeletedMembersOlderThan(thresholdDate);

            if (membersToDelete.isEmpty()) {
                log.info("삭제할 회원이 없습니다.");
                return;
            }

            int batchSize = 100;
            for (int i = 0; i < membersToDelete.size(); i += batchSize) {
                int end = Math.min(i + batchSize, membersToDelete.size());
                List<Member> batch = membersToDelete.subList(i, end);
                memberRepository.deleteAll(batch);
                log.info("배치 삭제 완료: 삭제된 회원 수={}", batch.size());
            }

            log.info("소프트 삭제된 회원 중 30일 이상 지난 회원 삭제 작업 완료: 총 삭제된 회원 수={}", membersToDelete.size());
        } catch (Exception e) {
            log.error("소프트 삭제된 회원 삭제 작업 중 예외 발생: ", e);
            throw new CustomException(ErrorCode.DATABASE_ACCESS_FAILED, e);
        }
    }
}
