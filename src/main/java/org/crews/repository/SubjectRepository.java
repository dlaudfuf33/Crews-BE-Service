package org.crews.repository;

import org.crews.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("SELECT s FROM Subject s JOIN FETCH s.interestings WHERE s.id>1L")
    List<Subject> findAllWithInterestings();

}
