package org.example.newsfeed.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;
}
