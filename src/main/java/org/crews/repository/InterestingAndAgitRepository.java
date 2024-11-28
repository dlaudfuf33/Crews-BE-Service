package org.crews.repository;

import org.crews.model.InterestingAndAgit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestingAndAgitRepository extends JpaRepository<InterestingAndAgit, Long> {
    @Modifying
    @Query("DELETE FROM InterestingAndAgit m WHERE m.agit.id = :agitId")
    void deleteByAgitIdCustom(@Param("agitId") Long agitId);
}
