package org.example.newsfeed.domain.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.auth.anotation.Auth;
import org.example.newsfeed.domain.auth.dto.AuthUser;
import org.example.newsfeed.domain.comment.dto.CommentRequest;
import org.example.newsfeed.domain.comment.dto.CommentResponse;
import org.example.newsfeed.domain.comment.entity.Comment;
import org.example.newsfeed.domain.comment.service.CommentService;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

   @PostMapping
    public ResponseEntity<CommentResponse> save(
            @Auth AuthUser authUser,
            @RequestBody CommentRequest request
   ) {
       CommentResponse saveComment = commentService.save(authUser.getId(), request);
       return ResponseEntity.ok(saveComment);
   }

   @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> update(
            @Auth AuthUser authUser,
            @PathVariable Long commentId,
            @RequestBody @Valid CommentRequest request
   ) {
       CommentResponse updateComment = commentService.update(authUser.getId(), commentId, request);
       return ResponseEntity.ok(updateComment);
   }

   @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @Auth AuthUser authUser,
            @PathVariable Long commentId
   ) {
       commentService.delete(authUser.getId(), commentId);
       return ResponseEntity.noContent().build();
   }
}
