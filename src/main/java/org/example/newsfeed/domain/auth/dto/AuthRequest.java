package org.example.newsfeed.domain.auth.dto;

import lombok.Getter;

@Getter
public class AuthRequest {

    private String email;
    private String name;
    private String password;
}
