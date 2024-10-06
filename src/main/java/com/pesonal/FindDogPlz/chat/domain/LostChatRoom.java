package com.pesonal.FindDogPlz.chat.domain;

import com.pesonal.FindDogPlz.global.common.BaseDateEntity;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LostChatRoom extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private LostPost lostPost;

    @OneToOne(fetch = FetchType.EAGER)
    private LostChat lastChatMsg;

    @ManyToOne
    private Member member1;

    @ManyToOne
    private Member member2;

    @Builder
    public LostChatRoom(LostPost lostPost, Member roomMaker, Member invitee) {
        this.lostPost = lostPost;
        this.member1 = roomMaker;
        this.member2 = invitee;
    }

    public Member getReceiver(Long senderId) {
        return member1.getId().equals(senderId) ? member2 : member1;
    }

    public boolean isNewlyCreated() {
        return lastChatMsg == null;
    }
}
