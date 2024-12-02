package org.crews.repository;

import org.crews.model.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByFeedIdAndMemberId(Long feedId, Long memberId);
}
