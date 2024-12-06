package org.crews.repository;

import org.crews.model.Agit;
import org.crews.model.Member;
import org.crews.model.Membership;
import org.crews.model.constants.AgitRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberShipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByAgitAndAgitRole(Agit agit, AgitRole role);

    Optional<Membership> findByMemberAndAgit(Member member, Agit agit);

    @EntityGraph(attributePaths = {"member"})
    List<Membership> findByAgit(Agit agit);

    @EntityGraph(attributePaths = {"agit", "agit.agitAndAccount", "agit.agitAndAccount.account"})
    List<Membership> findByMember(Member member);

    @Query("SELECT DISTINCT m " +
            "FROM Membership m " +
            "JOIN FETCH m.agit a " +
            "LEFT JOIN FETCH a.subject " +
            "LEFT JOIN FETCH a.introducing " +
            "LEFT JOIN FETCH a.interestingAndAgits ia " +
            "LEFT JOIN FETCH ia.interesting " +
            "WHERE m.member.id = :memberid")
    List<Membership> findMembershipsWithAgitDetailsByMemberId(@Param("memberid") Long memberId);

    List<Membership> findTop3ByAgitAndAgitRoleLike(Agit build, AgitRole agitRole);

    List<Membership> findTop3ByAgitAndAgitRoleNot(Agit build, AgitRole agitRole);

    Long countByAgitAndAgitRoleNot(Agit build, AgitRole agitRole);

    Long countByAgitAndAgitRoleLike(Agit build, AgitRole agitRole);


    @Query("SELECT m FROM Membership m " +
            "JOIN FETCH m.agit a " +
            "WHERE m.member.id = :memberId AND m.agitRole <> :agitRole")
    List<Membership> findByMemberIdAndAgitRoleNot(
            @Param("memberId") Long memberId,
            @Param("agitRole") AgitRole agitRole
    );

    @Query("""
                SELECT m
                FROM Membership m
                LEFT JOIN FETCH m.agit a
                LEFT JOIN FETCH a.agitAndAccount aa
                LEFT JOIN FETCH aa.account acc
                LEFT JOIN FETCH acc.bank bank
                LEFT JOIN FETCH a.commonDues cd
                WHERE m.member.id = :memberId
            """)
    List<Membership> findMembershipsWithDetailsByMemberId(Long memberId);

    @Query("""
    SELECT m
    FROM Membership m
    LEFT JOIN FETCH m.member
    LEFT JOIN FETCH m.agit a
    LEFT JOIN FETCH a.agitAndAccount aa
    LEFT JOIN FETCH aa.account acc
    LEFT JOIN FETCH acc.bank
    WHERE m.member.id = :memberId AND m.agit.id = :agitId
""")
    Optional<Membership> findByMemberIdAndAgitId(@Param("memberId") Long memberId, @Param("agitId") Long agitId);

}

