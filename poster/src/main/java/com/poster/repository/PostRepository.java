package com.poster.repository;

import com.poster.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    List<Post> findAll();
    Optional<Post> findById(Long Id);
    List<Post> findAllByUserId(Long user_id);
    @Query("SELECT p FROM Post p WHERE p.user.id IN :following ORDER BY p.date DESC")
    List<Post> findPostsByFollowing(@Param("following") List<Long> following);
}
