package org.example.newsfeed.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.auth.anotation.Auth;
import org.example.newsfeed.domain.auth.dto.AuthUser;
import org.example.newsfeed.domain.user.dto.UserRequest;
import org.example.newsfeed.domain.user.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private UserService userService;

    //자기 자신만 변경 가능
    @PutMapping("/users")
    public void update(
            @Auth AuthUser authUser,
            @RequestBody UserRequest request
    ) {
        userService.update(authUser, request);
    }
}
