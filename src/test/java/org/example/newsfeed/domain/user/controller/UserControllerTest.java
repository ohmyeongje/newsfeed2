package org.example.newsfeed.domain.user.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.newsfeed.domain.auth.dto.AuthUser;
import org.example.newsfeed.domain.user.dto.UserRequest;
import org.example.newsfeed.domain.user.dto.UserResponse;
import org.example.newsfeed.domain.user.dto.UserUpdatePasswordRequest;
import org.example.newsfeed.domain.user.dto.UserUpdateResponse;
import org.example.newsfeed.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService; // @MockBean으로 변경

    /*@MockitoBean
    private EnableJpaAuditing jpaAuditingHandler;*/

    @MockitoBean
    private MappingContext jpaMappingContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자 조회 성공")
    void 사용자_조회_성공() throws Exception {
        // Given
        Long userId = 1L;
        AuthUser authUser = new AuthUser(userId, "test@example.com", "테스트 유저");
        UserResponse response = new UserResponse(userId, "test@example.com", "테스트 유저", 10, 5);

        given(userService.findUser(anyLong(), anyLong())).willReturn(response);

        // When & Then
        mockMvc.perform(get("/users/{userId}", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .requestAttr("authUser", authUser))  // 🔥 GET 요청에서는 content() 제거!
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("테스트 유저"));
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void 회원_정보_수정_성공() throws Exception {
        // Given
        Long userId = 1L;
        UserRequest request = new UserRequest("newemail@example.com", "새 이름");
        UserUpdateResponse response = new UserUpdateResponse(userId, "newemail@example.com", "새 이름", 10, 5, LocalDateTime.now());

        given(userService.update(any(AuthUser.class), eq(userId), any(UserRequest.class))).willReturn(response);

        // When & Then
        mockMvc.perform(put("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .requestAttr("authUser", new AuthUser(userId, "newemail@example.com", "새 이름")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비밀번호 수정 성공")
    void 비밀번호_수정_성공() throws Exception {
        // Given
        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest();
        ReflectionTestUtils.setField(request, "oldPassword", "oldPass123!");
        ReflectionTestUtils.setField(request, "newPassword", "newPass123!");
        ReflectionTestUtils.setField(request, "checkPassword", "newPass123!");

        doNothing().when(userService).updatePassword(any(AuthUser.class), any(UserUpdatePasswordRequest.class));

        // When & Then
        mockMvc.perform(put("/users/{userId}/password", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .requestAttr("authUser", new AuthUser(1L, "newemail@example.com", "새 이름")))
                .andExpect(status().isOk());
    }
}
