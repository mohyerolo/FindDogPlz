package com.pesonal.FindDogPlz.chat.dto;

import lombok.*;
import org.json.JSONObject;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessageReqDto {
    private Long chatRoomId;
    private String message;
    private MessageType type;

    public static ChatMessageReqDto toChatMessage(JSONObject jsonObject) {
        return ChatMessageReqDto.builder()
                .chatRoomId(Long.parseLong(String.valueOf(jsonObject.getInt("chatRoomId"))))
                .type(MessageType.valueOf(jsonObject.getString("type")))
                .message(jsonObject.getString("message"))
                .build();
    }
}
