package com.pesonal.FindDogPlz.chat.dto;

import com.pesonal.FindDogPlz.post.dto.PostSubDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomWithMessageDto {
    private Long chatRoomId;
    private String chatRoomName;

    private PostSubDto postInfo;

    private List<ChatMessageDto> chatMessageList;
}
