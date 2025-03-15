package org.example.newsfeed.domain.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.newsfeed.domain.feed.entity.Feed;

@Getter
@AllArgsConstructor
public class FeedPagingResponse {
    private Long id;
    private String title;
    private String content;
    private String userName;

    public static FeedPagingResponse fromEntity(Feed feed) {
        return new FeedPagingResponse(
                feed.getId(),
                feed.getTitle(),
                feed.getContent(),
                feed.getUser().getName()
        );
    }
}
