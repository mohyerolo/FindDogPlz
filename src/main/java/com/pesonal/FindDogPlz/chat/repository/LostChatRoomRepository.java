package com.pesonal.FindDogPlz.chat.repository;

import com.pesonal.FindDogPlz.chat.domain.LostChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LostChatRoomRepository extends JpaRepository<LostChatRoom, Long> {
    Optional<LostChatRoom> findByLostPostIdAndReporterId(Long lostPostId, Long receiverId);
}
