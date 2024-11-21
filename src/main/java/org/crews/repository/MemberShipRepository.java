package org.crews.repository;

import org.crews.model.Agit;
import org.crews.model.Member;
import org.crews.model.constants.MemberRole;
import org.crews.model.Membership;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberShipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByAgitAndRole(Agit agit, MemberRole role);
    Optional<Membership> findByMemberAndAgit(Member member, Agit agit);

    @EntityGraph(attributePaths = {"member"})
    List<Membership> findByAgit(Agit agit);

    @EntityGraph(attributePaths = {"agit", "agit.agitAndAccount", "agit.agitAndAccount.account"})
    List<Membership> findByMember(Member member);
}
