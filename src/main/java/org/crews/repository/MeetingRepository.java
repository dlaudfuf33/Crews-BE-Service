package org.crews.repository;

import org.crews.model.Agit;
import org.crews.model.Meeting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Slice<Meeting> findByAgitIdAndIsDeletedFalse(Long agitId, Pageable pageable);

    Optional<Meeting> findByIdAndAgit(Long meetingId, Agit agit);
}
