package org.example.newsfeed.domain.feed.repository;

import org.example.newsfeed.domain.feed.entity.Feed;
import org.example.newsfeed.domain.feed.entity.Like;
import org.example.newsfeed.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndFeed(User user, Feed feed); // 특정 유저가 특정 피드에 좋아용를 눌었는지 확인
    boolean existsByUserAndFeed(User user, Feed feed); // 좋아요 여부 확인
    void deleteByUserAndFeed(User user, Feed feed); // 좋아요 취소
}
