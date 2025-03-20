package org.example.newsfeed.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePasswordRequest {

    @NotBlank(message = "현재 비밀번호를 입력해주세요")
    private String oldPassword; //현재 비밀번호

    @Pattern(regexp ="^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,16}$",
            message = "새 비밀번호는 8~16자, 영문자, 숫자, 특수기호를 각각 하나 이상 포함해야 합니다.")
    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    private String newPassword; //새로운 비밀번호

    @NotBlank(message = "새 비밀번호를 확인해주세요")
    private String checkPassword; //비밀번호 확인
}
