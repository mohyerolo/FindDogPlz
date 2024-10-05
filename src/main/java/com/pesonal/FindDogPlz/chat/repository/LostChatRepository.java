package com.pesonal.FindDogPlz.chat.repository;

import com.pesonal.FindDogPlz.chat.domain.LostChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LostChatRepository extends JpaRepository<LostChat, Long> {

}
