package org.example.newsfeed.domain.feed.dto;

import lombok.Getter;

@Getter
public class LikeResponse {
    private final Long feedId;
    private final Long userId;
    private final boolean liked;

    public LikeResponse(Long feedId, Long userId, boolean liked) {
        this.feedId = feedId;
        this.userId = userId;
        this.liked = liked;
    }
}
