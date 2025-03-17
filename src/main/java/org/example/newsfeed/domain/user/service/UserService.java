package org.example.newsfeed.domain.user.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.common.encoder.PasswordEncoder;
import org.example.newsfeed.domain.auth.dto.AuthUser;
import org.example.newsfeed.domain.user.dto.UserRequest;
import org.example.newsfeed.domain.user.dto.UserResponse;
import org.example.newsfeed.domain.user.dto.UserUpdatePasswordRequest;
import org.example.newsfeed.domain.user.dto.UserUpdateResponse;
import org.example.newsfeed.domain.user.entity.User;
import org.example.newsfeed.domain.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserUpdateResponse update(AuthUser authUser, Long userId, UserRequest request) {

        //유저 존재 여부 확인.
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다.")
        );

        //본인의 프로필만 수정 가능.
        if (!authUser.getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 유저에 대한 프로필 수정 권한이 없습니다.");
        }

        //이미 존재하는 이메일.
        if (userRepository.existsByEmail(request.getEmail()) && !user.getEmail().equals(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다.");
        }


        //유저의 프로필 정보(이메일, 이름) 수정
        user.update(request.getEmail(), request.getName());

        //변경된 유저의 정보 반환.
        return new UserUpdateResponse(user.getId(),
                user.getEmail(),
                user.getName(),
                user.getFollowUser(),
                user.getFollowingUser(),
                user.getUpdatedAt()
        );
    }

    @Transactional
    public void updatePassword(AuthUser authUser, UserUpdatePasswordRequest request) {
        // 유저 조회
        User user = userRepository.findById(authUser.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다.")
        );

        // 현재 비밀번호 검증
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 & 확인 비밀번호 검증
        if (!request.getNewPassword().equals(request.getCheckPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "새 비밀번호가 일치하지 않습니다.");
        }

        // 기존 비밀번호와 동일한지 확인 (선택적)
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "기존 비밀번호와 동일한 비밀번호로 변경할 수 없습니다.");
        }

        // 새 비밀번호 암호화 후 저장
        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserResponse findUser(Long id, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다."));

        if (!id.equals(user.getId())) {
            return new UserResponse(user.getId(), user.getName(), user.getFollowUser(), user.getFollowingUser());
        }
        return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getFollowUser(), user.getFollowingUser());
    }
}