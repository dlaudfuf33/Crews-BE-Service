package org.crews.repository;

import org.crews.model.Agit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgitRepository extends JpaRepository<Agit, Long> {

    @Query("SELECT DISTINCT a FROM Agit a " +
            "JOIN FETCH a.introducing " +
            "JOIN FETCH a.subject " +
            "JOIN FETCH a.interestingAndAgits ia " +
            "JOIN FETCH ia.interesting " +
            "WHERE a.isDeleted = false")
    List<Agit> findAllWithFetchJoin();

    @Query("""
        SELECT a FROM Agit a
        LEFT JOIN FETCH a.commonDues cd
        WHERE a.id = :agitId AND a.isDeleted = false
    """)
    Optional<Agit> findByIdWithCommonDues(@Param("agitId") Long agitId);


}
