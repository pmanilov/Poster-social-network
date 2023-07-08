package com.poster.repository;

import com.poster.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Override
    List<Comment> findAll();
    Optional<Comment> findById(Long Id);
    List<Comment> findAllByPostId(Long post_id);
    List<Comment> findAllByUserId(Long user_id);
}
