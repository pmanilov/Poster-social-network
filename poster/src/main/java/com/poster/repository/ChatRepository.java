package com.poster.repository;

import com.poster.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long>{
    @Override
    List<Chat> findAll();
    Optional<Chat> findById(Long Id);
    @Query("SELECT c FROM Chat c WHERE c.secondUser.id = :user_id OR c.firstUser.id = :user_id")
    List<Chat> findAllByUserId(@Param("user_id") Long user_id);

    @Query("SELECT c FROM Chat c WHERE c.firstUser.id = :first_id AND c.secondUser.id = :second_id OR c.firstUser.id = :second_id AND c.secondUser.id = :first_id")
    Optional<Chat> findByUsersId(@Param("first_id") Long first_id, @Param("second_id") Long second_id);
}
