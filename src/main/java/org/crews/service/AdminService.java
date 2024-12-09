package org.crews.service;

import lombok.RequiredArgsConstructor;
import org.crews.dto.response.BanResponse;
import org.crews.dto.response.ReportedFeedResponse;
import org.crews.dto.response.ReportedFeedSliceResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.Member;
import org.crews.model.Report;
import org.crews.repository.MemberRepository;
import org.crews.repository.ReportRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;

    @Transactional(readOnly = true)
    public ReportedFeedSliceResponse getReports(
            Pageable pageable,
            Boolean isChecked
    ) {
        Slice<Report> reports;
        if (isChecked != null) {
            reports = reportRepository.findAllByIsCheckedOrderByUpdatedAtDesc(isChecked, pageable);
        } else {
            reports = reportRepository.findAllByOrderByUpdatedAtDesc(pageable);
        }
        return ReportedFeedSliceResponse.of(reports);
    }

    @Transactional
    public ReportedFeedResponse getReportDetailAndMarkAsChecked(Long id) {
        Report report = reportRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.REPORT_NOT_FOUNT, id.toString()
                ));

        if (!report.isChecked()) {
            report.setChecked(true);
            reportRepository.save(report);
        }

        return ReportedFeedResponse.of(report);
    }

    @Transactional
    public BanResponse banMember(Long memberId) {
        Member foundMember = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );
        if (foundMember.isBanned()) {
            throw new CustomException(ErrorCode.ALREADY_BANNED_MEMBER);
        }
        foundMember.setBanned(true);
        return BanResponse.from(foundMember);
    }
}
