package org.example.newsfeed.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.config.JwtUtil;
import org.example.newsfeed.common.encoder.PasswordEncoder;
import org.example.newsfeed.domain.auth.dto.AuthRequest;
import org.example.newsfeed.domain.auth.dto.AuthResponse;
import org.example.newsfeed.domain.user.entity.User;
import org.example.newsfeed.domain.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signup(AuthRequest request) {
        //이미 존재하는 이메일이므로 애러를 던짐
       if (userRepository.existsByEmail(request.getEmail())) {
           throw new IllegalArgumentException("이미 가입된 이메일 입니다");
       }


       //비밀번호 암호화
       String encoderPassword = passwordEncoder.encode(request.getPassword());
       User user = new User(request.getEmail(), request.getName(), encoderPassword);

        //유저 저장
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse signin(AuthRequest signinRequest) {

        User user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "회원의 정보를 찾을 수 없습니다.")
        );

        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "올바르지 않은 비밀번호입니다.");
        }

        String bearerJwt = jwtUtil.createToken(user.getId(), user.getEmail(), user.getName());
        String jwt = jwtUtil.substringToken(bearerJwt);

        return new AuthResponse(jwt);
    }
}
