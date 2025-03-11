package org.example.newsfeed.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.config.JwtUtil;
import org.example.newsfeed.common.encoder.PasswordEncoder;
import org.example.newsfeed.domain.auth.dto.AuthRequest;
import org.example.newsfeed.domain.auth.dto.AuthResponse;
import org.example.newsfeed.domain.user.entity.User;
import org.example.newsfeed.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
       User user = new User(request.getEmail(), encoderPassword);

        //유저 저장
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public AuthResponse signIn(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 회원입니다")
        );

        //비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 잘못되었습니다");
        }

        //비밀번호가 일치한 경우
        String bearerJwt = jwtUtil.createToken(user.getId(), user.getEmail());
        return new AuthResponse(bearerJwt);
    }
}
