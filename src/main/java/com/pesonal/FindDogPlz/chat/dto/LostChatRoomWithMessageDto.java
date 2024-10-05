package com.pesonal.FindDogPlz.chat.dto;

import com.pesonal.FindDogPlz.member.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LostChatRoomWithMessageDto {
    private Long chatRoomId;

    private MemberDto senderInfo;
    private MemberDto receiverInfo;

    private List<ChatMessageDto> chatMessageList;
}
