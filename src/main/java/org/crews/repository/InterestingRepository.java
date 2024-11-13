package org.crews.repository;

import org.crews.model.Interesting;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterestingRepository extends JpaRepository<Interesting,Long> {

    @EntityGraph(attributePaths = {"subject"})
    List<Interesting> findAll();
}
