package com.pesonal.FindDogPlz.chat.application;

import com.pesonal.FindDogPlz.chat.domain.ChatMessage;
import com.pesonal.FindDogPlz.chat.domain.ChatRoom;
import com.pesonal.FindDogPlz.chat.dto.ChatMessageDto;
import com.pesonal.FindDogPlz.chat.dto.ChatMessageReqDto;
import com.pesonal.FindDogPlz.chat.dto.ChatRoomWithMessageDto;
import com.pesonal.FindDogPlz.chat.repository.ChatQueryRepository;
import com.pesonal.FindDogPlz.chat.repository.ChatRepository;
import com.pesonal.FindDogPlz.chat.repository.ChatRoomRepository;
import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.member.domain.MemberAdapter;
import com.pesonal.FindDogPlz.member.repository.MemberRepository;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.post.dto.PostSubDto;
import com.pesonal.FindDogPlz.post.dto.PostType;
import com.pesonal.FindDogPlz.post.repository.LostPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MemberRepository memberRepository;
    private final LostPostRepository lostPostRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ChatQueryRepository chatQueryRepository;

    @Transactional
    public ChatRoomWithMessageDto enterChatRoom(Member sender, Long receiverId, PostType type, Long postId) {
        ChatRoom chatRoom = findOrCreateChatRoom(sender, receiverId, postId);
        Member receiver = chatRoom.getReceiver(sender.getId());

        ChatRoomWithMessageDto chatRoomDto = createBasicChatRoomDto(chatRoom, receiver.getName());
        if (!chatRoom.isNewlyCreated()) addChatMessages(chatRoomDto);
        return chatRoomDto;
    }

    private ChatRoom findOrCreateChatRoom(Member sender, Long receiverId, Long lostPostId) {
        return chatRoomRepository
                .findByLostPostIdAndSenderAndReceiver(lostPostId, sender.getId(), receiverId)
                .orElseGet(() -> createChatRoom(sender, receiverId, lostPostId));
    }

    private ChatRoom createChatRoom(Member sender, Long receiverId, Long lostPostId) {
        LostPost lostPost = lostPostRepository.findById(lostPostId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "실종 공고를 찾을 수 없습니다"));
        Member receiver = memberRepository.findById(receiverId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "해당 회원을 찾을 수 없습니다."));
        ChatRoom chatRoom = ChatRoom.lostBuilder().lostPost(lostPost).roomMaker(sender).invitee(receiver).lostBuild();
        return chatRoomRepository.save(chatRoom);
    }

    private ChatRoomWithMessageDto createBasicChatRoomDto(ChatRoom chatRoom, String receiverName) {
        return ChatRoomWithMessageDto.builder()
                .chatRoomId(chatRoom.getId())
                .postInfo(PostSubDto.fromLostPost(chatRoom.getLostPost()))
                .chatRoomName(receiverName)
                .build();
    }

    private void addChatMessages(ChatRoomWithMessageDto basicDto) {
        PageRequest pageRequest = PageRequest.ofSize(6);
        Slice<ChatMessageDto> chatList = chatQueryRepository.getChatsByLastLostChatId(basicDto.getChatRoomId(), null, pageRequest);
        basicDto.addChatMessage(chatList);
    }

    @Transactional
    public void saveMessage(ChatMessageReqDto dto, MemberAdapter sender) {
        ChatRoom chatRoom = chatRoomRepository.findById(dto.getChatRoomId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "채팅방이 존재하지 않습니다."));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender.getMember())
                .message(dto.getMessage())
                .checked(false)
                .build();
        chatRepository.save(chatMessage);

        chatRoom.updateLastChat(chatMessage);
    }
}
