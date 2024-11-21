package org.crews.repository;

import org.crews.model.MemberAndInteresting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberAndInterestingRepository
        extends JpaRepository<MemberAndInteresting, Long> {
    @Modifying
    @Query("DELETE FROM MemberAndInteresting m WHERE m.member.id = :memberId")
    void deleteByMemberIdCustom(@Param("memberId") Long memberId);}
