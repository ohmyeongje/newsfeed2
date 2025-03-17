package org.example.newsfeed.domain.follow.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.auth.anotation.Auth;
import org.example.newsfeed.domain.auth.dto.AuthUser;
import org.example.newsfeed.domain.follow.repository.FollowRepository;
import org.example.newsfeed.domain.follow.service.FollowService;
import org.example.newsfeed.domain.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follows")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    //팔로우
    @PostMapping("/{followingId}")
    public ResponseEntity<String> followUser(@Auth AuthUser authUser, @PathVariable Long followingId) {
        followService.followUser(authUser.getId(), followingId);
        return ResponseEntity.ok("팔로우 성공");
    }

    //언팔로우
    @DeleteMapping("/{followingId}")
    public ResponseEntity<String> unfollowUser(@Auth AuthUser authUser, @PathVariable Long followingId) {
        followService.unfollowUser(authUser.getId(), followingId);
        return ResponseEntity.ok("언팔로우 성공");
    }

    //내가 팔로우한 사용자 목록 조회
    @GetMapping("/{userId}/following")
    public ResponseEntity<List<User>> getFollowingUsers(@PathVariable Long userId) {
        return ResponseEntity.ok(followService.getFollowingUsers(userId));
    }
}
