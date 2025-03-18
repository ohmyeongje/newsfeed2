package org.example.newsfeed.domain.feed.repository;

import org.example.newsfeed.domain.feed.entity.Feed;
import org.example.newsfeed.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findByUserInOrderByCreatedAtDesc(List<User> users);
    List<Feed> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

}
