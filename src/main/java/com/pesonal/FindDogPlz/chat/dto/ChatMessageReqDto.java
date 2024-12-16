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

    public static ChatMessageReqDto toChatMessage(final JSONObject jsonObject) {
        return ChatMessageReqDto.builder()
                .chatRoomId(Long.parseLong(String.valueOf(jsonObject.getInt("chatRoomId"))))
                .message(jsonObject.getString("message"))
                .build();
    }
}
