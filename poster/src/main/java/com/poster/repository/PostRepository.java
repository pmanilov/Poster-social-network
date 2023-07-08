package com.poster.repository;

import com.poster.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    List<Post> findAll();
    Optional<Post> findById(Long Id);
    List<Post> findAllByUserId(Long user_id);
}
