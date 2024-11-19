package org.crews.repository;

import org.crews.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Slice<Event> findByAgitId(Long agitId, Pageable pageable);
}
