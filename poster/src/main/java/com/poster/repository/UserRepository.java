package com.poster.repository;

import com.poster.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    @Transactional
    @Modifying
    @Query(value = """
                    INSERT INTO subscriptions (follower_id, following_id) VALUES (:userId, :targetUserId)
            """, nativeQuery = true)
    void addSubscription(Long userId, Long targetUserId);

    @Transactional
    @Modifying
    @Query(value = """
                    DELETE FROM subscriptions where follower_id = :userId AND following_id = :targetUserId
            """, nativeQuery = true)
    void unSubscribe(Long userId, Long targetUserId);


    @Query(value = """
           SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
           FROM subscriptions s
           WHERE s.follower_id = :userId AND s.following_id = :targetUserId
           """, nativeQuery = true)
    boolean isUserSubscribed(Long userId, Long targetUserId);



}
