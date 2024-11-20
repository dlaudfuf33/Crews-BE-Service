package org.crews.repository;

import org.crews.model.CommonDues;
import org.crews.model.Dues;
import org.crews.model.Membership;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DuesRepository extends JpaRepository<Dues, Long> {
    @EntityGraph(attributePaths = {"membership", "membership.member"})
    List<Dues> findByCommonDues(CommonDues commonDues);

    List<Dues> findByMembershipAndCommonDues(Membership membership, CommonDues commonDues);
}
