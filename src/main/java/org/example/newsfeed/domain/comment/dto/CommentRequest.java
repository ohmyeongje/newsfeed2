package org.example.newsfeed.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.example.newsfeed.domain.comment.entity.Comment;

@Getter
public class CommentRequest {
    @NotNull(message = "피드 ID는 필수입니다.")
    private Long feedId;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    @Builder
    public CommentRequest(Long feedId, String content) {
        this.feedId = feedId;
        this.content = content;
    }
}
