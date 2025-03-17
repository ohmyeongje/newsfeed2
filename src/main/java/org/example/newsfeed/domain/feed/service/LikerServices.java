package org.example.newsfeed.domain.feed.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.auth.dto.AuthUser;
import org.example.newsfeed.domain.feed.dto.LikeRequest;
import org.example.newsfeed.domain.feed.dto.LikeResponse;
import org.example.newsfeed.domain.feed.entity.Feed;
import org.example.newsfeed.domain.feed.entity.Like;
import org.example.newsfeed.domain.feed.repository.FeedRepository;
import org.example.newsfeed.domain.feed.repository.LikeRepository;
import org.example.newsfeed.domain.user.entity.User;
import org.example.newsfeed.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikerServices {

    private final FeedRepository feedRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    @Transactional
    public LikeResponse toggleLike(Long userId, LikeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));
        Feed feed = feedRepository.findById(request.getFeedId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 피드입니다"));

        //좋아요 여부 확인
        Optional<Like> existingLike = likeRepository.findByUserAndFeed(user, feed);

        if (existingLike.isPresent()) {
            //이미 좋아요가 되어 있으면 취소
            likeRepository.delete(existingLike.get());
            return new LikeResponse(feed.getId(), user.getId(), false);
        } else {
            //좋아요 추가
            Like like = new Like(user, feed);
            likeRepository.save(like);
            return new LikeResponse(feed.getId(), user.getId(), true);
        }
    }

    @Transactional(readOnly = true)
    public boolean isLiked(Long userId, Long feedId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 피드입니다"));

        return likeRepository.existsByUserAndFeed(user, feed);
    }
}
