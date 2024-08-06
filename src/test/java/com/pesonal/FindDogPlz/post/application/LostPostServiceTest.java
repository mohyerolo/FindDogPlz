package com.pesonal.FindDogPlz.post.application;

import com.pesonal.FindDogPlz.global.common.Gender;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.member.dto.SignUpDto;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.post.dto.LostPostReqDto;
import com.pesonal.FindDogPlz.post.repository.LostPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LostPostServiceTest {

    @InjectMocks
    private LostPostService lostPostService;

    @Mock
    private LostPostRepository lostPostRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Member member;

    @BeforeEach
    public void setup() {
        SignUpDto dto = new SignUpDto("garamId", "password", "garam", "010-1111-1111");
        member = Member.builder()
                .signUpDto(dto)
                .pwEncoder(passwordEncoder)
                .build();
    }

    @Test
    void createLostPost_잃어버린곳_equal_마지막위치() {
        LostPostReqDto dto = new LostPostReqDto("강아지", "흰색이고 말티즈, 미용 안함", Gender.F, true, false,
                "경기도", 37.468873, 126.853229,
                "경기도", 37.468873, 126.853229, LocalDateTime.now());

        lostPostService.createLostPost(dto, member);
        ArgumentCaptor<LostPost> lostPostCaptor = ArgumentCaptor.forClass(LostPost.class);

        verify(lostPostRepository, times(1)).save(lostPostCaptor.capture());
        LostPost savedLostPost = lostPostCaptor.getValue();
        assertEquals(savedLostPost.getLostPoint(), savedLostPost.getFinalPoint());
    }

    @Test
    void createLostPost_잃어버린곳_notEqual_마지막위치() {
        LostPostReqDto dto = new LostPostReqDto("강아지", "흰색이고 말티즈, 미용 안함", Gender.F, true, false,
                "경기도", 37.468873, 126.853229,
                "경기도 서울", 38.468873, 126.853229, LocalDateTime.now());

        lostPostService.createLostPost(dto, member);
        ArgumentCaptor<LostPost> lostPostCaptor = ArgumentCaptor.forClass(LostPost.class);

        verify(lostPostRepository, times(1)).save(lostPostCaptor.capture());
        LostPost savedLostPost = lostPostCaptor.getValue();
        assertNotEquals(savedLostPost.getLostPoint(), savedLostPost.getFinalPoint());
    }

}