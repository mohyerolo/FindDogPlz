package com.pesonal.FindDogPlz.chat.repository;

import com.pesonal.FindDogPlz.chat.dto.ChatMessageDto;
import com.pesonal.FindDogPlz.chat.dto.QChatMessageDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pesonal.FindDogPlz.chat.domain.QLostChat.lostChat;

@Repository
@RequiredArgsConstructor
public class ChatQueryRepositoryImpl implements ChatQueryRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<ChatMessageDto> getChatsByLastLostChatId(Long lostChatRoomId, Long lastLostChatId, Pageable pageable) {
        List<ChatMessageDto> results = jpaQueryFactory.select(new QChatMessageDto(lostChat.id, lostChat.sender.id, lostChat.sender.name, lostChat.message, lostChat.checked))
                .from(lostChat)
                .join(lostChat.sender)
                .where(lostChat.chatRoom.id.eq(lostChatRoomId),
                        ltLostChatId(lastLostChatId)
                )
                .orderBy(lostChat.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private BooleanExpression ltLostChatId(Long lastLostChatId) {
        return lastLostChatId != null ? lostChat.id.lt(lastLostChatId) : null;
    }
}
