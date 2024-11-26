package org.crews.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.request.FeedRequest;
import org.crews.dto.response.AgitVaildationResponse;
import org.crews.dto.response.FeedResponse;
import org.crews.dto.response.FeedSliceResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.Feed;
import org.crews.repository.FeedRepository;
import org.crews.utils.CheckExceptionUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final CheckExceptionUtil checkExceptionUtil;

    public FeedSliceResponse getAllFeeds(Long memberId, Long agitId, int page) {
        AgitVaildationResponse checkedResult = checkExceptionUtil.checkAgitException(memberId, agitId);

        Slice<Feed> feeds = feedRepository.findByAgitIdAndIsDeletedFalse(agitId, PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdAt"))));
        return FeedSliceResponse.of(checkedResult.getMember(), feeds);
    }

    public FeedResponse getFeed(Long memberId, Long agitId, Long feedId) {
        AgitVaildationResponse checkedResult = checkExceptionUtil.checkAgitException(memberId, agitId);

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new CustomException(ErrorCode.FEED_NOT_FOUND));
        if(feed.isDeleted()) throw new CustomException(ErrorCode.DELETED_FEED);

        return FeedResponse.of(checkedResult.getMember(), feed);
    }

    @Transactional
    public FeedResponse postFeed(Long memberId, Long agitId, FeedRequest feedRequest) {
        AgitVaildationResponse checkedResult = checkExceptionUtil.checkAgitException(memberId, agitId);

        Feed feed = Feed.of(feedRequest, checkedResult.getAgit(), checkedResult.getMember());
        return FeedResponse.of(checkedResult.getMember(), feedRepository.save(feed));
    }
}
