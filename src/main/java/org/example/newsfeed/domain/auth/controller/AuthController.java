package org.example.newsfeed.domain.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.auth.dto.AuthRequest;
import org.example.newsfeed.domain.auth.dto.AuthResponse;
import org.example.newsfeed.domain.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public void signup(
            @RequestBody AuthRequest request
            ) {
        authService.signup(request);
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<AuthResponse> signIn(
            @RequestBody AuthRequest request
    ) {
       return ResponseEntity.ok(authService.signIn(request));
    }
}
