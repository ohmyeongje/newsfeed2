package org.example.newsfeed.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentUpdateRequest {

    @NotBlank(message = "수정할 내용을 입력해주세요")
    private String newContent;

    @Builder
    public CommentUpdateRequest(String newContent) {
        this.newContent = newContent;
    }
}
