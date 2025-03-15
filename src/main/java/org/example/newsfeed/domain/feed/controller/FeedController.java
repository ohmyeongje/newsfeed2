package org.example.newsfeed.domain.feed.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.auth.anotation.Auth;
import org.example.newsfeed.domain.auth.dto.AuthUser;
import org.example.newsfeed.domain.feed.dto.FeedPagingResponse;
import org.example.newsfeed.domain.feed.dto.FeedRequest;
import org.example.newsfeed.domain.feed.dto.FeedResponse;
import org.example.newsfeed.domain.feed.service.FeedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @PostMapping
    public ResponseEntity<FeedResponse> save(
            @Auth AuthUser authUser,
            @RequestBody FeedRequest request
            ) {
        FeedResponse saveFeed = feedService.save(authUser.getId(), request);
        return ResponseEntity.ok(saveFeed);
    }

    // 뉴스피드 목록 조회 (페이징 적용)
    @GetMapping
    public ResponseEntity<Page<FeedPagingResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<FeedPagingResponse> feeds = feedService.findAll(pageRequest);
        return ResponseEntity.ok(feeds);
    }
    
    @GetMapping("/{feedId}")
    public ResponseEntity<FeedResponse> getFeed(@PathVariable Long feedId) {
        return ResponseEntity.ok(feedService.getFeed(feedId));
    }

    @PutMapping("/{feedId}")
    public ResponseEntity<FeedResponse> updateFeed(
            @Auth AuthUser authUser,
            @PathVariable Long feedID,
            @RequestBody FeedRequest request
    ) {
        return ResponseEntity.ok(feedService.updateFeed(authUser.getId(), feedID, request));
    }

    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> deleteFeed(@Auth AuthUser authUser, @PathVariable Long feedId) {
        feedService.deleteFeed(authUser.getId(), feedId);
        return ResponseEntity.noContent().build();
    }
}
