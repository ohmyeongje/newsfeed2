package org.example.newsfeed.domain.comment.entity;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.newsfeed.common.BaseEntity;
import org.example.newsfeed.domain.feed.entity.Feed;
import org.example.newsfeed.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    private String content;

    @Builder
    public Comment(Feed feed, User user, String content) {
        this.feed = feed;
        this.user = user;
        this.content = content;
    }

    public void update(String content) {
        this.content = content;
    }
}
