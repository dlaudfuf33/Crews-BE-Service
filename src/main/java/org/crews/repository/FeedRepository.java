package org.crews.repository;

import org.crews.model.Feed;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    Slice<Feed> findByAgitIdAndIsDeletedFalse(Long agitId, Pageable pageable);
}
