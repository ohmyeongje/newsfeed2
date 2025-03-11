package org.example.newsfeed.domain.user.dto;

import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String email;
    private final String password;

    public UserResponse(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
