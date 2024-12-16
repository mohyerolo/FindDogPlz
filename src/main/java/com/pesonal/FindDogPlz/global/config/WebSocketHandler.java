package com.pesonal.FindDogPlz.global.config;

import com.pesonal.FindDogPlz.chat.application.ChatService;
import com.pesonal.FindDogPlz.chat.dto.ChatMessageReqDto;
import com.pesonal.FindDogPlz.member.domain.MemberAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final Map<Long, ConcurrentHashMap<String, WebSocketSession>> chatRoomSessionMap = new ConcurrentHashMap<>();
    private final ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        MemberAdapter memberAdapter = (MemberAdapter) session.getAttributes().get("sender");
        List<Long> chatRoomList = chatService.getAllChatRoomAsMember(memberAdapter.getMember());

        for (Long id : chatRoomList) {
            ConcurrentHashMap<String, WebSocketSession> chatRoomSessions = getChatRoomSessions(id);
            chatRoomSessions.putIfAbsent(session.getId(), session);
        }
    }

    private ConcurrentHashMap<String, WebSocketSession> getChatRoomSessions(final Long chatRoomId) {
        return chatRoomSessionMap.computeIfAbsent(chatRoomId, k -> new ConcurrentHashMap<>());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        chatRoomSessionMap.forEach((chatRoomId, chatRoomSessions) -> {
            chatRoomSessions.remove(session.getId());
            if (chatRoomSessions.isEmpty()) {
                chatRoomSessionMap.remove(chatRoomId);
            }
        });
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
        ChatMessageReqDto chatMessageReqDto = ChatMessageReqDto.toChatMessage(new JSONObject(message.getPayload()));
        MemberAdapter memberAdapter = (MemberAdapter) session.getAttributes().get("sender");

        List<WebSocketSession> chatRoomSessions = getSessionsWithoutSelf(chatMessageReqDto.getChatRoomId(), session.getId());

        chatService.saveMessage(chatMessageReqDto, memberAdapter.getMember());
        sendMessageToChatRoom(message, chatRoomSessions);
    }

    private List<WebSocketSession> getSessionsWithoutSelf(final Long chatRoomId, final String sessionId) {
        return getChatRoomSessions(chatRoomId).entrySet().stream()
                .filter(entry -> !entry.getKey().equals(sessionId))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private void sendMessageToChatRoom(final TextMessage message, final List<WebSocketSession> chatRoomSessions) {
        chatRoomSessions.forEach(session -> {
            try {
                session.sendMessage(message);
            } catch (IOException e) {
                log.error("Failed to send message: {}, error: {}", message, e.getMessage());
            }
        });
    }
}
