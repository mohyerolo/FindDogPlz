package com.pesonal.FindDogPlz.chat.application;

import com.pesonal.FindDogPlz.chat.domain.LostChatRoom;
import com.pesonal.FindDogPlz.chat.dto.ChatRoomWithMessageDto;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LostChatService {

    private final MemberRepository memberRepository;
    private final LostPostRepository lostPostRepository;
    private final LostChatRoomRepository lostChatRoomRepository;
    private final LostChatRepository lostChatRepository;

    @Transactional
    public ChatRoomWithMessageDto enterChatRoom(Member sender, Long receiverId, Long lostPostId) {
        LostChatRoom lostChatRoom = lostChatRoomRepository
                .findByLostPostIdAndReporterId(lostPostId, receiverId)
                .orElseGet(() -> createChatRoom(sender, receiverId, lostPostId));

        Member receiver = lostChatRoom.getReceiver(sender.getId());

        // TODO: 새로 만든 게 아닐때는 Slice 사용해서 채팅 메시지 가져오게
        return lostChatRoom.isNewlyCreated() ? createBasicChatRoomWithMessageDto(lostChatRoom, receiver.getName())
                : null;
    }

    private LostChatRoom createChatRoom(Member sender, Long receiverId, Long lostPostId) {
        LostPost lostPost = lostPostRepository.findById(lostPostId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "실종 공고를 찾을 수 없습니다"));
        Member receiver = memberRepository.findById(receiverId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "해당 회원을 찾을 수 없습니다."));
        LostChatRoom lostChatRoom = LostChatRoom.builder().lostPost(lostPost).roomMaker(sender).invitee(receiver).build();
        return lostChatRoomRepository.save(lostChatRoom);
    }

    private ChatRoomWithMessageDto createBasicChatRoomWithMessageDto(LostChatRoom lostChatRoom, String receiverName) {
        return ChatRoomWithMessageDto.builder()
                .chatRoomId(lostChatRoom.getId())
                .postInfo(PostSubDto.lostPostBuilder().lostPost(lostChatRoom.getLostPost()).lostPostBuild())
                .chatRoomName(receiverName)
                .build();
    }

}
