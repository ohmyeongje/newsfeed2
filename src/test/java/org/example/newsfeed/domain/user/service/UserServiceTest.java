package org.example.newsfeed.domain.user.service;

import org.aspectj.lang.annotation.Before;
import org.example.newsfeed.NewsfeedApplication;
import org.example.newsfeed.common.encoder.PasswordEncoder;
import org.example.newsfeed.domain.auth.dto.AuthRequest;
import org.example.newsfeed.domain.auth.dto.AuthUser;
import org.example.newsfeed.domain.user.dto.UserRequest;
import org.example.newsfeed.domain.user.dto.UserResponse;
import org.example.newsfeed.domain.user.dto.UserUpdatePasswordRequest;
import org.example.newsfeed.domain.user.dto.UserUpdateResponse;
import org.example.newsfeed.domain.user.entity.User;
import org.example.newsfeed.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // LENIENT 설정
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setup() {
        // 테스트용 User 객체 생성
        user = new User("test@example.com", "testUser", "encodedPassword");
    }

    @Test
    public void 회원_정보_수정_성공() {
        // given: 테스트용 사용자 데이터
        User user = new User("test@example.com", "testUser", "password");
        ReflectionTestUtils.setField(user, "id", 1L); // ID 강제 설정

        AuthUser authUser = new AuthUser(1L, "test@example.com", "testUser");

        UserRequest request = new UserRequest("newemail@example.com", "testUser2");

        // 🔹 Mock 설정 추가
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);

        // when: 사용자 프로필 수정
        UserUpdateResponse response = userService.update(authUser, 1L, request);

        // then: 사용자 정보가 수정되었는지 확인
        assertEquals("newemail@example.com", response.getEmail());
        assertEquals("testUser2", response.getName());
    }


    @Test
    void 잘못된_사용자_프로필_수정_권한() {
        // given
        AuthUser authUser = new AuthUser(2L, "other@example.com", "testUser");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // when & then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.update(authUser, 1L, new UserRequest())
        );
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    }

    @Test
    public void 비밀번호_수정_성공() {
        // given: 테스트용 사용자 데이터
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        User user = new User("test@example.com", "testUser", passwordEncoder.encode(oldPassword));
        ReflectionTestUtils.setField(user, "id", 1L); // ID 강제 설정

        AuthUser authUser = new AuthUser(1L, "test@example.com", "testUser");

        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest();
        ReflectionTestUtils.setField(request, "oldPassword", oldPassword);
        ReflectionTestUtils.setField(request, "newPassword", newPassword);
        ReflectionTestUtils.setField(request, "checkPassword", newPassword);

        // 🔹 Mock 설정 추가
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
        when(passwordEncoder.matches(newPassword, user.getPassword())).thenReturn(false);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        // 비밀번호 암호화 Mock 설정 추가
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // 비밀번호 변경 실행
        userService.updatePassword(authUser, request);

        // 변경된 사용자 정보 가져오기
        User updatedUser = userRepository.findById(authUser.getId()).orElseThrow();

        // 로그 출력 (디버깅용)
        System.out.println("입력한 새 비밀번호: " + newPassword);
        System.out.println("저장된 비밀번호: " + updatedUser.getPassword());
        System.out.println("비밀번호 일치 여부: " + passwordEncoder.matches(newPassword, updatedUser.getPassword()));

// 검증 수정 (암호화된 값과 비교)
        assertTrue(passwordEncoder.matches(newPassword, updatedUser.getPassword()));

// save() 호출 여부 검증
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    void 비밀번호_일치하지_않음() {
        // given
        AuthUser authUser = new AuthUser(1L, "test@example.com", "wrongPassword");
        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest();
        request.setOldPassword("wrongPassword");
        request.setNewPassword("NewPass123!");
        request.setCheckPassword("NewPass123!");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // when & then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.updatePassword(authUser, request)
        );
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Test
    public void 사용자_조회_성공() {
        // given: 테스트용 사용자 데이터
        User user = new User("test@example.com", "testUser", "password");
        ReflectionTestUtils.setField(user, "id", 1L); // ID 강제 설정
        ReflectionTestUtils.setField(user, "followUser", 10); // 팔로워 수 설정
        ReflectionTestUtils.setField(user, "followingUser", 20); // 팔로잉 수 설정

        // 🔹 Mock 설정 추가
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when: 사용자 조회
        UserResponse response = userService.findUser(1L, 1L);

        // then: 사용자 정보 조회 확인
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getName(), response.getName());
        assertEquals(user.getFollowUser(), response.getFollowerUser());
        assertEquals(user.getFollowingUser(), response.getFollowingUser());
    }

    @Test
    void 존재하지_않는_사용자_조회() {
        // given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                userService.findUser(999L, 999L)
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}