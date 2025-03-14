package org.example.newsfeed.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.auth.anotation.Auth;
import org.example.newsfeed.domain.auth.dto.AuthUser;
import org.example.newsfeed.domain.user.dto.UserRequest;
import org.example.newsfeed.domain.user.dto.UserUpdatePasswordRequest;
import org.example.newsfeed.domain.user.service.UserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //자기 자신만 변경 가능
    @PutMapping("/users/{userId}")
    public void update(
            @Auth AuthUser authUser,
            @PathVariable Long userId,
            @RequestBody UserRequest request
    ) {
        userService.update(authUser, userId, request);
    }

    @PutMapping("/users/{userId}/password")
    public void updatePassword(
            @Auth AuthUser authUser,
            @RequestBody @Valid UserUpdatePasswordRequest request
    ) {
        userService.updatePassword(authUser, request);
    }
}
