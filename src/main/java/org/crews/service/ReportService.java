package org.crews.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.ReportRequest;
import org.crews.dto.response.AgitValidationResponse;
import org.crews.dto.response.ReportResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.Feed;
import org.crews.model.Member;
import org.crews.model.Report;
import org.crews.repository.ReportRepository;
import org.crews.utils.CheckExceptionUtil;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final CheckExceptionUtil checkExceptionUtil;

    @Transactional
    public ReportResponse reportFeed(Long memberId, Long feedId, Long agitId, ReportRequest reportRequest){

        AgitValidationResponse checkedResult = checkExceptionUtil.checkFeedException(memberId,agitId, feedId);

        Feed feed = checkedResult.getFeed();
        Member member = checkedResult.getMember();

        if(feed.isDeleted()){
            throw new CustomException(ErrorCode.DELETED_FEED);
        }
        if (reportRepository.existsByFeedAndMember(feed, member)) {
            throw new CustomException(ErrorCode.ALREADY_REPORTED_FEED);
        }
        Report report = Report.of(reportRequest, feed, member);
        return ReportResponse.from(reportRepository.save(report));
    }
}
