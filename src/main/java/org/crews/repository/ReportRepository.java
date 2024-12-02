package org.crews.repository;

import org.crews.model.Feed;
import org.crews.model.Member;
import org.crews.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    boolean existsByFeedAndMember(Feed feed, Member member);
}
