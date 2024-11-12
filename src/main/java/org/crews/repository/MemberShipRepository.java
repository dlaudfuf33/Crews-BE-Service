package org.crews.repository;

import org.crews.model.Agit;
import org.crews.model.MemberRole;
import org.crews.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberShipRepository extends JpaRepository<Membership, Long> {
    Optional<Membership> findByAgitAndRole(Agit agit, MemberRole role);
}
