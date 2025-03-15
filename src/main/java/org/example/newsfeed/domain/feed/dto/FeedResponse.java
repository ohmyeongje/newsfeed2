package org.example.newsfeed.domain.feed.dto;

import lombok.Getter;
import org.example.newsfeed.domain.feed.entity.Feed;
import org.example.newsfeed.domain.user.entity.User;

import java.time.LocalDateTime;

@Getter
public class FeedResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public FeedResponse(Feed feed) {
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.author = feed.getUser().getEmail();
        this.createdAt = feed.getCreatedAt();
        this.updatedAt = feed.getUpdatedAt();
    }
}
