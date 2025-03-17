package org.example.newsfeed.domain.comment.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.domain.comment.dto.CommentRequest;
import org.example.newsfeed.domain.comment.dto.CommentResponse;
import org.example.newsfeed.domain.comment.entity.Comment;
import org.example.newsfeed.domain.comment.repository.CommentRepository;
import org.example.newsfeed.domain.feed.entity.Feed;
import org.example.newsfeed.domain.feed.repository.FeedRepository;
import org.example.newsfeed.domain.user.entity.User;
import org.example.newsfeed.domain.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse save(Long userId, CommentRequest request) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"));

        Feed feed = feedRepository.findById(request.getFeedId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "피드를 찾을 수 없습니다"));
        Comment comment = new Comment(feed, user, request.getContent());
        commentRepository.save(comment);
        return new CommentResponse(comment);
    }

    @Transactional
    public CommentResponse update(Long userId, Long commentId, @Valid CommentRequest request) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "댓글 수정 권한이 없습니다");
        }

        comment.update(request.getContent());
        return new CommentResponse(comment);
    }

    @Transactional
    public void delete(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "댓글 삭제 권한이 없습니다");
        }

        commentRepository.delete(comment);
    }

    @Transactional
    public int likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다"));
        comment.like();
        commentRepository.save(comment);
        return comment.getLikeCount();
    }

    @Transactional
    public int unlikeCount(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다"));
        comment.unlike();
        commentRepository.save(comment);
        return comment.getLikeCount();
    }

    @Transactional(readOnly = true)
    public int getLikeCount(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다"));
        return comment.getLikeCount();
    }
}
