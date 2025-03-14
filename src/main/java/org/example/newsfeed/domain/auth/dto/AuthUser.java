package org.example.newsfeed.domain.auth.dto;

import lombok.Getter;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private String name;

    public AuthUser(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
