package org.example.newsfeed.domain.follow.repository;

import org.example.newsfeed.domain.follow.entity.Follow;
import org.example.newsfeed.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    List<Follow> findByFollower(User follower);
    void deleteByFollowerAndFollowing(User follower, User following);
}
