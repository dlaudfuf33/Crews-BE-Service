package org.crews.repository;

import org.crews.model.Agit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgitRepository extends JpaRepository<Agit, Long> {
}
