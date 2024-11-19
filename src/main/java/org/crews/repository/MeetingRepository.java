package org.crews.repository;

import org.crews.model.Meeting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Slice<Meeting> findByAgitId(Long agitId, Pageable pageable);
}
