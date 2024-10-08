package com.pesonal.FindDogPlz.chat.repository;

import com.pesonal.FindDogPlz.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatMessage, Long> {

}
