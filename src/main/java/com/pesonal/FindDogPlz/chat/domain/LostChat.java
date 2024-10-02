package com.pesonal.FindDogPlz.chat.domain;

import com.pesonal.FindDogPlz.global.common.BaseDateEntity;
import com.pesonal.FindDogPlz.member.domain.Member;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LostChat extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member sender;

    @ManyToOne
    private LostChatRoom chatRoom;

    @NotBlank
    private String message;

    @NotNull
    private boolean read;

}