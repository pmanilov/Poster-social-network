package com.poster.repository;

import com.poster.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p ORDER BY p.date DESC")
    List<Post> findAll();

    @Query("SELECT p FROM Post p LEFT JOIN p.likedBy l GROUP BY p.id ORDER BY COUNT(l) DESC, p.date DESC")
    List<Post> findAllOrderByLikesCountDesc();
    Optional<Post> findById(Long Id);

    List<Post> findAllByUserId(@Param("user")Long user_id);

    @Query(value = """
           SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
           FROM likes l
           WHERE l.post_id = :postId AND l.user_id = :userId
           """, nativeQuery = true)
    boolean isPostLikedByUser(Long postId, Long userId);

    @Query("SELECT p FROM Post p WHERE p.user.id IN :following ORDER BY p.date DESC")
    List<Post> findPostsByFollowing(@Param("following") List<Long> following);

    @Query("SELECT p FROM Post p LEFT JOIN p.likedBy l WHERE p.user.id IN :following GROUP BY p.id ORDER BY COUNT(l) DESC, p.date DESC")
    List<Post> findPostsByFollowingOrderByLikesCountDesc(@Param("following") List<Long> following);
}