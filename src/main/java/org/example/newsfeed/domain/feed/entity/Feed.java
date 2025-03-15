package org.example.newsfeed.domain.feed.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.newsfeed.common.BaseEntity;
import org.example.newsfeed.domain.auth.dto.AuthUser;
import org.example.newsfeed.domain.user.entity.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feed extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Feed(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
}
