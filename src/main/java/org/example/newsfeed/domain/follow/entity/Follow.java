package org.example.newsfeed.domain.follow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.newsfeed.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "follows")
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follwer_id", nullable = false)
    private User follower; //나

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private User following; //내가 팔로우 하는 사람

    public Follow(User follower, User following) {
        this.follower =follower;
        this.following = following;
    }

}
