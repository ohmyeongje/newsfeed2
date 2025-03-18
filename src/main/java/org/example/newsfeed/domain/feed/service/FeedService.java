package org.example.newsfeed.domain.feed.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.comment.repository.CommentRepository;
import org.example.newsfeed.domain.feed.dto.FeedPagingResponse;
import org.example.newsfeed.domain.feed.dto.FeedRequest;
import org.example.newsfeed.domain.feed.dto.FeedResponse;
import org.example.newsfeed.domain.feed.entity.Feed;
import org.example.newsfeed.domain.feed.repository.FeedRepository;
import org.example.newsfeed.domain.follow.service.FollowService;
import org.example.newsfeed.domain.user.entity.User;
import org.example.newsfeed.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final FollowService followService;

    //피드 생성
    @Transactional
    public FeedResponse save(Long userId, FeedRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        Feed feed = new Feed(request.getTitle(), request.getContent(), user);
        feedRepository.save(feed);
        return new FeedResponse(feed);
    }

    //피드 조회
    @Transactional(readOnly = true)
    public Page<FeedPagingResponse> findAll(Pageable pageable) {
        return feedRepository.findAll(pageable)
                .map(FeedPagingResponse::fromEntity);
    }

    //특정 피드 조회
    @Transactional(readOnly = true)
    public FeedResponse getFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new IllegalArgumentException("피드를 찾을 수 없습니다"));
        return new FeedResponse(feed);
    }

    //날짜별 검색
    @Transactional(readOnly = true)
    public List<FeedResponse> getFeedsByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Feed> feeds = feedRepository.findByCreatedAtBetween(startDateTime, endDateTime);
        return feeds.stream()
                .map(FeedResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //피드 수정
    @Transactional
    public FeedResponse updateFeed(Long userId, Long feedID, FeedRequest request) {
        Feed feed = feedRepository.findById(feedID).orElseThrow(
                () -> new IllegalArgumentException("피드를 찾을 수 없습니다"));
        if (!feed.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        feed.setTitle(request.getTitle());
        feed.setContent(request.getContent());
        return new FeedResponse(feed);


    }

    //피드 삭제
    @Transactional
    public void deleteFeed(Long userId, Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드를 찾을 수 없습니다"));
        if (!feed.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다");
        }
        feedRepository.delete(feed);
    }

    //뉴스피드 조회
    @Transactional(readOnly = true)
    public List<Feed> getNewsFeed(Long userId) {
        List<User> followingUsers = followService.getFollowingUsers(userId);
        return feedRepository.findByUserInOrderByCreatedAtDesc(followingUsers);
    }
}
