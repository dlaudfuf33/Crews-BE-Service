package org.crews.repository;

import org.crews.model.Agit;
import org.crews.model.Member;
import org.crews.model.constants.AgitRole;
import org.crews.model.Membership;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByAgitAndAgitRole(Agit agit, AgitRole role);

    Optional<Membership> findByMemberAndAgit(Member member, Agit agit);
    Optional<Membership> findByMemberIdAndAgitId(Long memberId, Long agitId);
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
}
