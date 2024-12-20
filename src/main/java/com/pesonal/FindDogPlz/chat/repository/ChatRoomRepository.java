package com.pesonal.FindDogPlz.chat.repository;

import com.pesonal.FindDogPlz.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT ch FROM ChatRoom ch WHERE ch.lostPost.id = :lostPostId " +
            "AND ((ch.member1.id = :senderId AND ch.member2.id = :receiverId) " +
            "OR (ch.member1.id = :receiverId AND ch.member2.id = :senderId))")
    Optional<ChatRoom> findByLostPostIdAndSenderAndReceiver(
            @Param("lostPostId") Long lostPostId,
            @Param("senderId") Long senderId,
            @Param("receiverId") Long receiverId
    );

    @Query("SELECT ch FROM ChatRoom ch WHERE ch.findPost.id = :findPostId " +
            "AND ((ch.member1.id = :senderId AND ch.member2.id = :receiverId) " +
            "OR (ch.member1.id = :receiverId AND ch.member2.id = :senderId))")
    Optional<ChatRoom> findByFindPostIdAndSenderAndReceiver(
            @Param("findPostId") Long findPostId,
            @Param("senderId") Long senderId,
            @Param("receiverId") Long receiverId
    );

    @Query("SELECT c FROM ChatRoom c WHERE c.member1.id = :memberId OR c.member2.id = :memberId")
    List<ChatRoom> findByRelatedMember(@Param("memberId") Long memberId);
}
