package com.example.webchatapplication.repository;

import com.example.webchatapplication.entity.ChatRoomEntity;
import com.example.webchatapplication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    @Query("SELECT c FROM ChatRoomEntity c join c.userEntities u where u.id = :userId AND c.chatRoomType = 'PRIVATE'")
    List<ChatRoomEntity> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT c FROM ChatRoomEntity c JOIN c.userEntities u1 JOIN c.userEntities u2 WHERE u1.id = :firstUserId AND u2.id = :secondUserId AND c.chatRoomType = 'PRIVATE'")
    Optional<ChatRoomEntity> findPrivateChatBetweenUsers(@Param("firstUserId") Long firstUserId, @Param("secondUserId") Long secondUserId);
}
