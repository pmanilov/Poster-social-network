package com.poster.repository;

import com.poster.model.UserImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.*;
import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    Optional<UserImage> findByUserId(Long userId);
    void  deleteUserImageByUserId(Long userId);

    @Query(value = """
           SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END
           FROM user_image i
           WHERE i.user_id = :userId
           """, nativeQuery = true)
    boolean isUserHasPhoto(Long userId);

}
