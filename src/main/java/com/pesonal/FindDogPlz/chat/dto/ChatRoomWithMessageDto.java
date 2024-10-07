package com.pesonal.FindDogPlz.chat.dto;

import com.pesonal.FindDogPlz.post.dto.PostSubDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Slice;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomWithMessageDto {
    private Long chatRoomId;
    private String chatRoomName;

    private PostSubDto postInfo;

    private Slice<ChatMessageDto> chatMessageList;

    public void addChatMessage(Slice<ChatMessageDto> list) {
        this.chatMessageList = list;
    }
}
