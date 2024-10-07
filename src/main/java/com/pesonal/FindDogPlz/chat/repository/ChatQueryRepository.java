package com.pesonal.FindDogPlz.chat.repository;

import com.pesonal.FindDogPlz.chat.dto.ChatMessageDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatQueryRepository {
    Slice<ChatMessageDto> getChatsByLastLostChatId(Long lostChatRoomId, Long lastLostChatId, Pageable pageable);
}
