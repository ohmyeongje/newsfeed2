package org.example.newsfeed.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.auth.anotation.Auth;
import org.example.newsfeed.domain.auth.dto.AuthUser;
import org.example.newsfeed.domain.user.dto.UserRequest;
import org.example.newsfeed.domain.user.dto.UserResponse;
import org.example.newsfeed.domain.user.dto.UserUpdatePasswordRequest;
import org.example.newsfeed.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //자기 자신만 변경 가능
    @PutMapping
    public void update(
            @Auth AuthUser authUser,
            @PathVariable Long userId,
            @RequestBody UserRequest request
    ) {
        userService.update(authUser, userId, request);
    }

    @GetMapping
    public ResponseEntity<UserResponse> findUser(
            @Auth AuthUser authUser,
            @PathVariable Long userId)
    {
       return ResponseEntity.ok(userService.findUser(authUser.getId(), userId));
    }

    @PutMapping("/password")
    public void updatePassword(
            @Auth AuthUser authUser,
            @RequestBody @Valid UserUpdatePasswordRequest request
    ) {
        userService.updatePassword(authUser, request);
    }
}
