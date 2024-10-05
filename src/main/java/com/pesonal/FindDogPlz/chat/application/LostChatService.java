package com.pesonal.FindDogPlz.chat.application;

import com.pesonal.FindDogPlz.chat.domain.LostChatRoom;
import com.pesonal.FindDogPlz.chat.dto.LostChatRoomWithMessageDto;
import com.pesonal.FindDogPlz.chat.repository.LostChatRepository;
import com.pesonal.FindDogPlz.chat.repository.LostChatRoomRepository;
import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.member.repository.MemberRepository;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.post.repository.LostPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LostChatService {

    private final MemberRepository memberRepository;
    private final LostPostRepository lostPostRepository;
    private final LostChatRoomRepository lostChatRoomRepository;
    private final LostChatRepository lostChatRepository;

    @Transactional
    public LostChatRoomWithMessageDto enterChatRoom(Member sender, Long receiverId, Long lostPostId) {
        Optional<LostChatRoom> optionalLostChatRoom = lostChatRoomRepository.findByLostPostIdAndReporterId(lostPostId, receiverId);
        LostChatRoom lostChatRoom = optionalLostChatRoom.orElseGet(() -> createChatRoom(sender, lostPostId));
        return null;
    }

    // 공고로 새로운 채팅 신청은 게시글 작성자가 아닌 사람만 가능한 기능임
    private LostChatRoom createChatRoom(Member sender, Long lostPostId) {
        LostPost lostPost = lostPostRepository.findById(lostPostId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "실종 공고를 찾을 수 없습니다"));
        LostChatRoom lostChatRoom = LostChatRoom.builder().lostPost(lostPost).reporter(sender).build();
        return lostChatRoomRepository.save(lostChatRoom);
    }
}
