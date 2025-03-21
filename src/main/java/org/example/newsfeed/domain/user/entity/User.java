package org.example.newsfeed.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.newsfeed.common.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int followUser;

    @Column(nullable = false)
    private int followingUser;

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public void update(String email, String name) {
        this.email = email;
        this.name = name;
    }
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void increaseFollower() {
        followUser++;
    }

    public void increaseFollowing() {
        followingUser++;
    }

    public void decreaseFollower() {
        followUser--;
    }

    public void decreaseFollowing() {
        followingUser--;
    }
}
