package org.crews.repository;

import org.crews.model.Feed;
import org.crews.model.Heart;
import org.crews.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByFeedAndMember(Feed feed, Member member);
}
