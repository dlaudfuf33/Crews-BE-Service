package org.crews.repository;

import org.crews.model.Agit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgitRepository extends JpaRepository<Agit, Long> {

    @Query("SELECT DISTINCT a FROM Agit a " +
            "JOIN FETCH a.introducing " +
            "JOIN FETCH a.subject " +
            "JOIN FETCH a.interestingAndAgits ia " +
            "JOIN FETCH ia.interesting " +
            "WHERE a.isDeleted = false")
    List<Agit> findAllWithFetchJoin();

    Slice<Agit> findByIntroductionLikeAndIsDeletedFalse(String keyWord, Pageable pageable);

    @Query("SELECT a FROM Agit a " +
            "WHERE a.isDeleted = false " +
            "AND a.introduction LIKE %:keyword% " +
            "AND a.id NOT IN (" +
            "    SELECT m.agit.id FROM Membership m WHERE m.member.id = :memberId" +
            ")")
    Slice<Agit> findByKeywordAndNotJoined(@Param("keyword") String keyword, @Param("memberId") Long memberId, Pageable pageable);
}
