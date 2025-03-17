package org.example.newsfeed.domain.feed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "feed_like")
public class Like {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    public Like(User user, Feed feed) {
        this.user = user;
        this.feed = feed;
    }
}
