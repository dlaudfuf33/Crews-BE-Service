package org.crews.repository;

import org.crews.model.Agit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
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

    @Query("SELECT DISTINCT a FROM Agit a " +
            "JOIN FETCH a.introducing " +
            "JOIN FETCH a.subject " +
            "JOIN FETCH a.interestingAndAgits ia " +
            "JOIN FETCH ia.interesting " +
            "WHERE a.isDeleted = false " +
            "AND (:memberId IS NULL OR a.address.id = (SELECT m.address.id FROM Member m WHERE m.id = :memberId)) " +
            "AND (:memberId IS NULL OR a NOT IN (SELECT m.agit FROM Membership m WHERE m.member.id = :memberId)) " +
            "ORDER BY a.createdAt DESC")
    List<Agit> findNewAgitsForMember(@Param("memberId") Long memberId);

    @Query("SELECT DISTINCT a FROM Agit a " +
            "JOIN FETCH a.introducing " +
            "JOIN FETCH a.subject " +
            "JOIN FETCH a.interestingAndAgits ia " +
            "JOIN FETCH ia.interesting " +
            "WHERE a.isDeleted = false " +
            "AND (:memberId IS NULL OR a.address.id = (SELECT m.address.id FROM Member m WHERE m.id = :memberId)) " +
            "AND (:memberId IS NULL OR a NOT IN (SELECT m.agit FROM Membership m WHERE m.member.id = :memberId)) " +
            "ORDER BY a.currentPerson DESC")
    List<Agit> findRecruitAgitsForMember(@Param("memberId") Long memberId);



}
