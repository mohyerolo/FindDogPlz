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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {
    private final Map<Long, ConcurrentHashMap<String, WebSocketSession>> chatRoomSessionMap = new ConcurrentHashMap<>();
    private final ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long chatRoomId = getChatRoomId(session);
        ConcurrentHashMap<String, WebSocketSession> chatRoomSessions = getChatRoomSessions(chatRoomId);
        chatRoomSessions.putIfAbsent(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long chatRoomId = getChatRoomId(session);
        ConcurrentHashMap<String, WebSocketSession> chatRoomSessions = chatRoomSessionMap.get(chatRoomId);
        if (chatRoomSessions != null) {
            chatRoomSessions.remove(session.getId());
        }
    }

    private Long getChatRoomId(WebSocketSession session) {
        return Long.parseLong((String) session.getAttributes().get("chatRoomId"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject jsonObject = new JSONObject(message.getPayload());
        ChatMessageReqDto chatMessageReqDto = ChatMessageReqDto.toChatMessage(jsonObject);
        MemberAdapter memberAdapter = (MemberAdapter) session.getAttributes().get("sender");

        chatService.saveMessage(chatMessageReqDto, memberAdapter.getMember());

        Long chatRoomId = chatMessageReqDto.getChatRoomId();
        ConcurrentHashMap<String, WebSocketSession> chatRoomSessions = getChatRoomSessions(chatRoomId);

        sendMessageToChatRoom(jsonObject, chatRoomSessions);
    }

    private ConcurrentHashMap<String, WebSocketSession> getChatRoomSessions(Long chatRoomId) {
        return chatRoomSessionMap.computeIfAbsent(chatRoomId, k -> new ConcurrentHashMap<>());
    }

    private void sendMessageToChatRoom(JSONObject chatMessageDto, ConcurrentHashMap<String, WebSocketSession> chatRoomSessions) {
        chatRoomSessions.forEach((key, value) -> {
            TextMessage textMessage = new TextMessage(chatMessageDto.toString());
            try {
                value.sendMessage(textMessage);
            } catch (IOException e) {
                log.error("Failed to send message: {}, error: {}", textMessage, e.getMessage());
            }
        });
    }
}
