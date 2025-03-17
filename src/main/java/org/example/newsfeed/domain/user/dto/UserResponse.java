package org.example.newsfeed.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final int followerUser;
    private final int followingUser;

    public UserResponse(Long id, String email, String name, int followerUser, int followingUser) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.followerUser = followerUser;
        this.followingUser = followingUser;
    }

    public UserResponse(Long id, String name, int followerUser, int followingUser) {
        this.id = id;
        this.email = null;
        this.name = name;
        this.followerUser = followerUser;
        this.followingUser = followingUser;
    }
}
