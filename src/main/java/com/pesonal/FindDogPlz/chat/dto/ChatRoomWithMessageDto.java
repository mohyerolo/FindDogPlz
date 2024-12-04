package com.pesonal.FindDogPlz.chat.dto;

import com.pesonal.FindDogPlz.post.dto.PostSubDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Long chatRoomId;

    @NotBlank
    private String chatRoomName;

    @NotNull
    @Valid
    private PostSubDto postInfo;

    @NotNull
    @Valid
    private Slice<ChatMessageDto> chatMessageList;

    public void addChatMessage(Slice<ChatMessageDto> list) {
        this.chatMessageList = list;
    }
}
