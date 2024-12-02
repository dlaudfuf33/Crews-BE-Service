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
import org.crews.model.Heart;
import org.crews.repository.FeedRepository;
import org.crews.repository.HeartRepository;
import org.crews.utils.CheckExceptionUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final HeartRepository heartRepository;
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

    public FeedResponse editFeed(Long memberId, Long agitId, Long feedId, FeedRequest feedRequest) {
        AgitVaildationResponse checkedResult = checkExceptionUtil.checkAgitException(memberId, agitId);
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                ()-> new CustomException(ErrorCode.FEED_NOT_FOUND));
        if(feed.isDeleted()) throw new CustomException(ErrorCode.DELETED_FEED);
        if(!feed.getMember().getId().equals(memberId)){
            throw new CustomException(ErrorCode.AUTHORIZED_FEED_UPDATE);
        }
        feed.update(feedRequest);
        return FeedResponse.of(checkedResult.getMember(), feedRepository.save(feed));
    }

    public ResponseEntity<String> deleteFeed(Long memberId,  Long feedId){

        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new CustomException(ErrorCode.FEED_NOT_FOUND));

        if(feed.isDeleted()) throw new CustomException(ErrorCode.DELETED_FEED);

        if(!feed.getMember().getId().equals(memberId)){
            throw new CustomException(ErrorCode.AUTHORIZED_FEED_DELETE);
        }

        feed.setDeleted(true);
        feedRepository.save(feed);

        return ResponseEntity.ok("기록을 삭제하였습니다.");
    }

    public String toggleHeartFeed(Long memberId, Long agitId, Long feedId){
        AgitVaildationResponse checkedResult = checkExceptionUtil.checkAgitException(memberId, agitId);
        Feed feed = feedRepository.findById(feedId).orElseThrow(()->new CustomException(ErrorCode.FEED_NOT_FOUND));
        if(feed.isDeleted()) throw new CustomException(ErrorCode.DELETED_FEED);

        Optional<Heart>existingHeart= heartRepository.findByFeedIdAndMemberId(feedId,memberId);
        if(existingHeart.isPresent()){
            heartRepository.delete(existingHeart.get());
            feed.setLikeCount(feed.getLikeCount()-1);
            feedRepository.save(feed);
            return "좋아요 등록 취소";
        }else{
            Heart newHeart=Heart.builder().feed(feed).member(checkedResult.getMember()).build();
            heartRepository.save(newHeart);
            feed.setLikeCount(feed.getLikeCount()+1);
            feedRepository.save(feed);
            return "좋아요 등록 완료";
        }
    }
}
