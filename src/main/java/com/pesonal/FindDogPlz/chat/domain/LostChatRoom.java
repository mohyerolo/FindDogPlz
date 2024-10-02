package com.pesonal.FindDogPlz.chat.domain;

import com.pesonal.FindDogPlz.global.common.BaseDateEntity;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LostChatRoom extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private LostPost lostPost;

    @ManyToOne
    private Member reporter;
}
