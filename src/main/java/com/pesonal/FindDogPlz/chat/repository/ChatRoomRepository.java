package com.pesonal.FindDogPlz.chat.repository;

import com.pesonal.FindDogPlz.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT l FROM ChatRoom l WHERE l.lostPost.id = :lostPostId " +
            "AND ((l.member1.id = :senderId AND l.member2.id = :receiverId) " +
            "OR (l.member1.id = :receiverId AND l.member2.id = :senderId))")
    Optional<ChatRoom> findByLostPostIdAndSenderAndReceiver(
            @Param("lostPostId") Long lostPostId,
            @Param("senderId") Long senderId,
            @Param("receiverId") Long receiverId
    );
}
