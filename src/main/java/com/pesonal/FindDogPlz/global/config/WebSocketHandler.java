package com.pesonal.FindDogPlz.global.config;

import com.pesonal.FindDogPlz.chat.application.ChatService;
import com.pesonal.FindDogPlz.chat.dto.ChatMessageReqDto;
import com.pesonal.FindDogPlz.chat.dto.MessageType;
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
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject jsonObject = new JSONObject(message.getPayload());
        ChatMessageReqDto chatMessageReqDto = ChatMessageReqDto.toChatMessage(jsonObject);
        MemberAdapter memberAdapter = (MemberAdapter) session.getAttributes().get("sender");

        Long chatRoomId = chatMessageReqDto.getChatRoomId();
        ConcurrentHashMap<String, WebSocketSession> chatRoomSessions = getChatRoomSessions(chatRoomId);

        MessageType messageType = chatMessageReqDto.getType();
        switch (messageType) {
            case ENTER -> chatRoomSessions.putIfAbsent(session.getId(), session);
            case CLOSE -> chatRoomSessions.remove(session.getId());
            default -> {
                chatService.saveMessage(chatMessageReqDto, memberAdapter.getMember());
                sendMessageToChatRoom(jsonObject, chatRoomSessions);
            }
        }
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
