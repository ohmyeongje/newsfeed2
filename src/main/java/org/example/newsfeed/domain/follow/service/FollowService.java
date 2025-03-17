package org.example.newsfeed.domain.follow.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.follow.entity.Follow;
import org.example.newsfeed.domain.follow.repository.FollowRepository;
import org.example.newsfeed.domain.user.entity.User;
import org.example.newsfeed.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;


    // 특정 사용자 팔로우
    @Transactional
    public void followUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new EntityNotFoundException("팔로우할 사용자를 찾을 수 없습니다."));

        if (followRepository.findByFollowerAndFollowing(follower, following).isPresent()) {
            throw new IllegalStateException("이미 팔로우한 사용자입니다.");
        }

        followRepository.save(new Follow(follower, following));

        follower.increaseFollowing();
        following.increaseFollower();
    }

    // 특정 사용자 언팔로우
    @Transactional
    public void unfollowUser(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new EntityNotFoundException("언팔로우할 사용자를 찾을 수 없습니다."));

        Optional<Follow> follow = followRepository.findByFollowerAndFollowing(follower, following);
        if (follow.isEmpty()) {
            throw new IllegalStateException("이미 언팔로우한 사용자입니다.");
        }

        followRepository.deleteByFollowerAndFollowing(follower, following);

        follower.decreaseFollowing();
        following.decreaseFollower();
    }

    // 내가 팔로우한 사용자 목록 조회
    @Transactional(readOnly = true)
    public List<User> getFollowingUsers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        List<User> followingUsers = followRepository.findByFollower(user).stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());

        System.out.println("팔로우 목록: " + followingUsers);
        return followingUsers;
    }
}

