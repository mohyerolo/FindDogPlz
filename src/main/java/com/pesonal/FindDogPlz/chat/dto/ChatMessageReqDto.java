package com.pesonal.FindDogPlz.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.json.JSONObject;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessageReqDto {
    @NotNull
    private Long chatRoomId;

    @NotBlank
    private String message;

    @NotNull
    private MessageType type;

    public static ChatMessageReqDto toChatMessage(JSONObject jsonObject) {
        return ChatMessageReqDto.builder()
                .chatRoomId(Long.parseLong(String.valueOf(jsonObject.getInt("chatRoomId"))))
                .type(MessageType.valueOf(jsonObject.getString("type")))
                .message(jsonObject.getString("message"))
                .build();
    }
}
