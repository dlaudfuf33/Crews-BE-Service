package org.crews.repository;

import org.crews.model.InterestingAndAgit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestingAndAgitRepository extends JpaRepository<InterestingAndAgit, Long> {
}
