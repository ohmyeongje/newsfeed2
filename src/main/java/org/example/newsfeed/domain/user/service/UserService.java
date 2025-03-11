package org.example.newsfeed.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.auth.dto.AuthUser;
import org.example.newsfeed.domain.user.dto.UserRequest;
import org.example.newsfeed.domain.user.entity.User;
import org.example.newsfeed.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;

    @Transactional
    public void update(AuthUser authUser, UserRequest request) {
        User user = userRepository.findById(authUser.getId()).orElseThrow(
                ()-> new IllegalArgumentException("존재하는 유저입니다")
        );

        //이메일은 유니크하기 떄문에 업데이트를 해야 한다면, 이미 존재하는 이메일인지 확인을 해야함
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일이기 때문에 업데이트 할 수 없습니다");
        }

       user.update(request.getEmail(), request.getPassword());
    }
}
