package org.crews.repository;

import org.crews.model.CommonDues;
import org.crews.model.Dues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DuesRepository extends JpaRepository<Dues, Long> {
    List<Dues> findByCommonDues(CommonDues commonDues);
}
