package org.crews.repository;

import org.crews.model.Agit;
import org.crews.model.Interesting;
import org.crews.model.InterestingAndAgit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestingAndAgitRepository extends JpaRepository<InterestingAndAgit, Long> {
    void deleteByAgitAndInterestingIn(Agit agit, List<Interesting> interestsToDelete);
}
