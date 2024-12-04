package com.pesonal.FindDogPlz.chat.dto;

import com.pesonal.FindDogPlz.member.dto.MemberDto;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageDto {
    @NotNull
    private Long chatId;

    @NotNull
    private MemberDto memberInfo;

    @NotBlank
    private String message;

    @NotNull
    private boolean checked;

    @QueryProjection
    public ChatMessageDto(Long chatId, Long memberId, String name,
                          String message, boolean checked) {
        this.chatId = chatId;
        this.memberInfo = new MemberDto(memberId, name);
        this.message = message;
        this.checked = checked;
    }
}
