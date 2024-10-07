package com.pesonal.FindDogPlz.chat.application;

import com.pesonal.FindDogPlz.chat.domain.LostChatRoom;
import com.pesonal.FindDogPlz.chat.dto.ChatMessageDto;
import com.pesonal.FindDogPlz.chat.dto.ChatRoomWithMessageDto;
import com.pesonal.FindDogPlz.chat.repository.ChatQueryRepository;
import com.pesonal.FindDogPlz.chat.repository.LostChatRepository;
import com.pesonal.FindDogPlz.chat.repository.LostChatRoomRepository;
import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.member.repository.MemberRepository;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.post.dto.PostSubDto;
import com.pesonal.FindDogPlz.post.repository.LostPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LostChatService {

    private final MemberRepository memberRepository;
    private final LostPostRepository lostPostRepository;
    private final LostChatRoomRepository lostChatRoomRepository;
    private final LostChatRepository lostChatRepository;
    private final ChatQueryRepository chatQueryRepository;

    @Transactional
    public ChatRoomWithMessageDto enterChatRoom(Member sender, Long receiverId, Long lostPostId) {
        LostChatRoom lostChatRoom = findOrCreateChatRoom(sender, receiverId, lostPostId);
        Member receiver = lostChatRoom.getReceiver(sender.getId());

        ChatRoomWithMessageDto chatRoomDto = createBasicChatRoomDto(lostChatRoom, receiver.getName());
        if (!lostChatRoom.isNewlyCreated()) addChatMessages(chatRoomDto);
        return chatRoomDto;
    }

    private LostChatRoom findOrCreateChatRoom(Member sender, Long receiverId, Long lostPostId) {
        return lostChatRoomRepository
                .findByLostPostIdAndSenderAndReceiver(lostPostId, sender.getId(), receiverId)
                .orElseGet(() -> createChatRoom(sender, receiverId, lostPostId));
    }

    private LostChatRoom createChatRoom(Member sender, Long receiverId, Long lostPostId) {
        LostPost lostPost = lostPostRepository.findById(lostPostId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "실종 공고를 찾을 수 없습니다"));
        Member receiver = memberRepository.findById(receiverId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "해당 회원을 찾을 수 없습니다."));
        LostChatRoom lostChatRoom = LostChatRoom.builder().lostPost(lostPost).roomMaker(sender).invitee(receiver).build();
        return lostChatRoomRepository.save(lostChatRoom);
    }

    private ChatRoomWithMessageDto createBasicChatRoomDto(LostChatRoom lostChatRoom, String receiverName) {
        return ChatRoomWithMessageDto.builder()
                .chatRoomId(lostChatRoom.getId())
                .postInfo(PostSubDto.fromLostPost(lostChatRoom.getLostPost()))
                .chatRoomName(receiverName)
                .build();
    }

    private void addChatMessages(ChatRoomWithMessageDto basicDto) {
        PageRequest pageRequest = PageRequest.ofSize(6);
        Slice<ChatMessageDto> chatList = chatQueryRepository.getChatsByLastLostChatId(basicDto.getChatRoomId(), null, pageRequest);
        basicDto.addChatMessage(chatList);
    }
}