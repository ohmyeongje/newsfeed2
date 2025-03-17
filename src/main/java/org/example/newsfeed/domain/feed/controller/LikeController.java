package org.example.newsfeed.domain.feed.controller;

import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.auth.anotation.Auth;
import org.example.newsfeed.domain.auth.dto.AuthUser;
import org.example.newsfeed.domain.feed.dto.LikeRequest;
import org.example.newsfeed.domain.feed.dto.LikeResponse;
import org.example.newsfeed.domain.feed.service.LikerServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikerServices likerServices;

    @PostMapping
    public ResponseEntity<LikeResponse> toggleLike(
            @Auth AuthUser authUser,
            @RequestBody LikeRequest request) {
        LikeResponse response = likerServices.toggleLike(authUser.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{feedId}")
    public ResponseEntity<Boolean> isLiked(
            @Auth AuthUser authUser,
            @PathVariable Long feedId) {
        boolean liked = likerServices.isLiked(authUser.getId(), feedId);
        return ResponseEntity.ok(liked);
    }
}
