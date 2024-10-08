package com.pesonal.FindDogPlz.global.config;

import com.pesonal.FindDogPlz.chat.application.ChatService;
import com.pesonal.FindDogPlz.chat.dto.ChatMessageReqDto;
import com.pesonal.FindDogPlz.member.domain.MemberAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
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
//    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS =
//            new ConcurrentHashMap<>();
    private final Map<Long, ConcurrentHashMap<String, WebSocketSession>> chatRoomSessionMap = new ConcurrentHashMap<>();
    private final ChatService chatService;

//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        CLIENTS.put(session.getId(), session);
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        CLIENTS.remove(session.getId());
//    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JSONObject jsonObject = new JSONObject(message.getPayload());
        ChatMessageReqDto chatMessageReqDto = ChatMessageReqDto.toChatMessage(jsonObject);
        chatService.saveMessage(chatMessageReqDto, (MemberAdapter) session.getAttributes().get("sender"));

        Long chatRoomId = chatMessageReqDto.getChatRoomId();
        if(!chatRoomSessionMap.containsKey(chatRoomId)){
            chatRoomSessionMap.put(chatRoomId, new ConcurrentHashMap<>());
        }

        ConcurrentHashMap<String, WebSocketSession> chatRoomSessions = chatRoomSessionMap.get(chatRoomId);
        if (!chatRoomSessions.containsKey(session.getId())) chatRoomSessions.put(session.getId(), session);

        sendMessageToChatRoom(jsonObject, chatRoomSessions);
    }

    private void sendMessageToChatRoom(JSONObject chatMessageDto, ConcurrentHashMap<String, WebSocketSession> chatRoomSessions) {
        chatRoomSessions.forEach((key, value) -> {
            TextMessage textMessage = new TextMessage(chatMessageDto.toString());
            try {
                value.sendMessage(textMessage);
            } catch (IOException e) {
                log.error(e.getMessage() + ": message {}" , textMessage);
            }
        });
    }
}
