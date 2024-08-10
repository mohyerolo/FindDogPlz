package com.pesonal.FindDogPlz.post.application;

import com.pesonal.FindDogPlz.global.common.Gender;
import com.pesonal.FindDogPlz.global.util.PointParser;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.post.dto.*;
import com.pesonal.FindDogPlz.post.repository.LostPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LostPostServiceTest {

    @InjectMocks
    private LostPostService lostPostService;

    @Mock
    private LostPostRepository lostPostRepository;

    private Member member;
    private LostPost lostPost;

    @BeforeEach
    void setup() {
        member = mock(Member.class);
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

    @Test
    void updateLostPost_위치변경() {
        setLostPost();
        when(lostPostRepository.findById(any())).thenReturn(Optional.of(lostPost));
        when(member.getId()).thenReturn(1L);

        String updateFeatures = "특징";
        String updateFinalLocation = "대전";
        Double updateFinalLatitude = 40.468873;

        LostPostUpdateDto dto = new LostPostUpdateDto("변경", updateFeatures, Gender.F, true, true, LocalDateTime.now(),
                new LostLocationUpdateDto("경기도", 37.468873, 126.853229),
                new FinalLocationUpdateDto(updateFinalLocation, updateFinalLatitude, 125.853300));

        lostPostService.updateLostPost(1L, dto, member);

        assertEquals(updateFeatures, lostPost.getFeatures());
        assertEquals(updateFinalLocation, lostPost.getFinalLocation());
        assertEquals(updateFinalLatitude, lostPost.getFinalPoint().getY());
        assertEquals("경기도", lostPost.getLostLocation());
    }

    @Test
    void getLostPostById() {
        setLostPost();
        when(lostPostRepository.findById(anyLong())).thenReturn(Optional.ofNullable(lostPost));
        when(member.getId()).thenReturn(1L);
        when(member.getName()).thenReturn("이름");

        LostPostDetailDto lostPostDetailDto = lostPostService.getLostPostById(1L);
        assertEquals("강아지", lostPostDetailDto.getAnimalName());
        assertEquals("이름", lostPostDetailDto.getWriterName());
    }

    @Test
    void getLostPostById_찾을수없음() {
        setLostPost();
        when(lostPostRepository.findById(anyLong())).thenReturn(Optional.empty());

        LostPostDetailDto lostPostDetailDto = lostPostService.getLostPostById(1L);
        assertNull(lostPostDetailDto);
    }

    private void setLostPost() {
        LostPostReqDto dto = new LostPostReqDto("강아지", "흰색이고 말티즈, 미용 안함", Gender.F, true, false,
                "경기도", 37.468873, 126.853229,
                "경기도 서울", 38.468873, 126.853229, LocalDateTime.now());
        lostPost = LostPost.builder()
                .dto(dto)
                .writer(member)
                .lostPoint(PointParser.parsePoint(dto.getLostLatitude(), dto.getLostLongitude()))
                .finalPoint(PointParser.parsePoint(dto.getFinalLatitude(), dto.getFinalLongitude()))
                .build();
    }
}