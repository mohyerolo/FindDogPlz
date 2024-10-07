package com.pesonal.FindDogPlz.chat.repository;

import com.pesonal.FindDogPlz.chat.domain.LostChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LostChatRoomRepository extends JpaRepository<LostChatRoom, Long> {
    @Query("SELECT l FROM LostChatRoom l WHERE l.lostPost.id = :lostPostId " +
            "AND ((l.member1.id = :senderId AND l.member2.id = :receiverId) " +
            "OR (l.member1.id = :receiverId AND l.member2.id = :senderId))")
    Optional<LostChatRoom> findByLostPostIdAndSenderAndReceiver(
            @Param("lostPostId") Long lostPostId,
            @Param("senderId") Long senderId,
            @Param("receiverId") Long receiverId
    );
}
