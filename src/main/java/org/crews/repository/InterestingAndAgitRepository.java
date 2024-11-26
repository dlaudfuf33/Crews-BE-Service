package org.crews.repository;

import org.crews.model.Agit;
import org.crews.model.Interesting;
import org.crews.model.InterestingAndAgit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestingAndAgitRepository extends JpaRepository<InterestingAndAgit, Long> {
    void deleteByAgit(Agit agit);

    @Modifying
    @Query("DELETE FROM InterestingAndAgit m WHERE m.agit.id = :agitId")
    void deleteByAgitIdCustom(@Param("agitId") Long agitId);
}
