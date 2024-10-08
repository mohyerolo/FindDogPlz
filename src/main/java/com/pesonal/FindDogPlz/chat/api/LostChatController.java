package com.pesonal.FindDogPlz.chat.api;

import com.pesonal.FindDogPlz.chat.application.ChatService;
import com.pesonal.FindDogPlz.chat.dto.ChatRoomWithMessageDto;
import com.pesonal.FindDogPlz.global.auth.AuthMember;
import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.dto.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats/lost")
@RequiredArgsConstructor
public class LostChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatRoomWithMessageDto> enterChatRoom(@AuthMember Member sender, @RequestParam Long receiverId, @RequestParam Long lostPostId) {
        if (sender == null) throw new CustomException(ErrorCode.AUTHENTICATION_ERROR);
        return ResponseEntity.ok(chatService.enterChatRoom(sender, receiverId, PostType.LOST, lostPostId));
    }
}
