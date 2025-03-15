package org.example.newsfeed.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.comment.entity.Comment;
import org.example.newsfeed.domain.feed.entity.Feed;
import org.example.newsfeed.domain.user.entity.User;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
public class CommentResponse {

    private Long id;
    private Long feedId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.feedId = comment.getFeed().getId();
        this.userId = comment.getUser().getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}

