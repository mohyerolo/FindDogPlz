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

import static com.pesonal.FindDogPlz.chat.domain.QChatMessage.chatMessage;

@Repository
@RequiredArgsConstructor
public class ChatQueryRepositoryImpl implements ChatQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<ChatMessageDto> getChatsByLastLostChatId(final Long chatRoomId, final Long lastLostChatId, final Pageable pageable) {
        List<ChatMessageDto> results = executeQuery(chatRoomId, lastLostChatId, pageable);

        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private List<ChatMessageDto> executeQuery(final Long chatRoomId, final Long lastLostChatId, final Pageable pageable) {
        return jpaQueryFactory.select(createQChatMessage())
                .from(chatMessage)
                .join(chatMessage.sender)
                .where(chatMessage.chatRoom.id.eq(chatRoomId),
                        ltLostChatId(lastLostChatId)
                )
                .orderBy(chatMessage.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    private BooleanExpression ltLostChatId(Long lastChatId) {
        return lastChatId != null ? chatMessage.id.lt(lastChatId) : null;
    }

    private QChatMessageDto createQChatMessage() {
        return new QChatMessageDto(chatMessage.id, chatMessage.sender.id,
                chatMessage.sender.name, chatMessage.message, chatMessage.checked);
    }
}
