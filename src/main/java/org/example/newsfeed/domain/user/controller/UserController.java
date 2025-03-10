package org.example.newsfeed.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.user.service.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private UserService userService;
}
