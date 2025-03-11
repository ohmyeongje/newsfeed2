package org.example.newsfeed.domain.auth.dto;

import lombok.Getter;

@Getter
public class AuthResponse {

   private final String bearerJwt;

    public AuthResponse(String bearerToken) {
        this.bearerJwt = bearerToken;
    }
}
