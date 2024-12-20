package com.pesonal.FindDogPlz.chat.domain;

import com.pesonal.FindDogPlz.global.common.BaseDateEntity;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.FindPost;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.post.dto.PostType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private LostPost lostPost;

    @ManyToOne(fetch = FetchType.LAZY)
    private FindPost findPost;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private PostType postType;

    private String lastChatMsg;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member1;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member2;

    @Builder(builderMethodName = "lostBuilder", buildMethodName = "lostBuild")
    public ChatRoom(LostPost lostPost, Member roomMaker, Member invitee) {
        this.lostPost = lostPost;
        this.postType = PostType.LOST;
        this.member1 = roomMaker;
        this.member2 = invitee;
    }

    @Builder(builderMethodName = "findBuilder", buildMethodName = "findBuild")
    public ChatRoom(FindPost findPost, Member roomMaker, Member invitee) {
        this.findPost = findPost;
        this.postType = PostType.FIND;
        this.member1 = roomMaker;
        this.member2 = invitee;
    }

    public Member getReceiver(final Long senderId) {
        return member1.getId().equals(senderId) ? member2 : member1;
    }

    public boolean isNewlyCreated() {
        return lastChatMsg == null;
    }

    public void updateLastChat(ChatMessage chatMessage) {
        this.lastChatMsg = chatMessage.getMessage();
    }
}
