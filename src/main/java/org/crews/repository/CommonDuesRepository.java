package org.crews.repository;

import org.crews.model.Agit;
import org.crews.model.CommonDues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommonDuesRepository extends JpaRepository<CommonDues, Long> {
    Optional<CommonDues> findByAgit(Agit agit);
}
