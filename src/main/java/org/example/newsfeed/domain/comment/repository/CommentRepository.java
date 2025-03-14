package org.example.newsfeed.domain.comment.repository;

import org.example.newsfeed.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByFeedId(Long feedId);
}
