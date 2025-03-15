package org.example.newsfeed.domain.feed.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.comment.repository.CommentRepository;
import org.example.newsfeed.domain.feed.dto.FeedPagingResponse;
import org.example.newsfeed.domain.feed.dto.FeedRequest;
import org.example.newsfeed.domain.feed.dto.FeedResponse;
import org.example.newsfeed.domain.feed.entity.Feed;
import org.example.newsfeed.domain.feed.repository.FeedRepository;
import org.example.newsfeed.domain.user.entity.User;
import org.example.newsfeed.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    @Transactional
    public FeedResponse save(Long userId, FeedRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        Feed feed = new Feed(request.getTitle(), request.getContent(), user);
        feedRepository.save(feed);
        return new FeedResponse(feed);
    }

    @Transactional(readOnly = true)
    public Page<FeedPagingResponse> findAll(Pageable pageable) {
        return feedRepository.findAll(pageable)
                .map(FeedPagingResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public FeedResponse getFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new IllegalArgumentException("피드를 찾을 수 없습니다"));
        return new FeedResponse(feed);
    }

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

    @Transactional
    public void deleteFeed(Long userId, Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("피드를 찾을 수 없습니다"));
        if (!feed.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("권한이 없습니다");
        }
        feedRepository.delete(feed);
    }
}
