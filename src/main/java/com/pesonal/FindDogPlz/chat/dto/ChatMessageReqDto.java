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

    public static ChatMessageReqDto toChatMessage(JSONObject jsonObject) {
        return ChatMessageReqDto.builder()
                .chatRoomId(Long.parseLong(String.valueOf(jsonObject.getInt("chatRoomId"))))
                .message(jsonObject.getString("message"))
                .build();
    }
}
