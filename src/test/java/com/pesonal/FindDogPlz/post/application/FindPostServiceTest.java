package com.pesonal.FindDogPlz.post.application;

import com.pesonal.FindDogPlz.global.common.Gender;
import com.pesonal.FindDogPlz.global.util.PointParser;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.FindPost;
import com.pesonal.FindDogPlz.post.dto.FindPostReqDto;
import com.pesonal.FindDogPlz.post.repository.FindPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPostServiceTest {

    @InjectMocks
    private FindPostService findPostService;

    @Mock
    private FindPostRepository findPostRepository;

    private Member member;
    private FindPost findPost;

    @BeforeEach
    void setup() {
        member = Mockito.mock(Member.class);
    }

    @Test
    void createFindPost() {
        FindPostReqDto dto = new FindPostReqDto("특징", Gender.M, false, false,
                "경기도", 37.468873, 126.853229, LocalDateTime.now());
        findPostService.createFindPost(dto, member);
        ArgumentCaptor<FindPost> captor = ArgumentCaptor.forClass(FindPost.class);
        verify(findPostRepository, times(1)).save(captor.capture());
        FindPost findPost1 = captor.getValue();
        assertEquals(dto.getFeatures(), findPost1.getFeatures());
    }

    @Test
    void updateFindPost_위치변경() {
        setFindPost();
        String updateFeatures = "특징 변경";
        String updateLocation = "서울";

        FindPostReqDto dto = new FindPostReqDto(updateFeatures, Gender.M, true, false,
                updateLocation, 37.468873, 127.853229, LocalDateTime.now());
        when(findPostRepository.findById(anyLong())).thenReturn(Optional.ofNullable(findPost));
        when(member.getId()).thenReturn(1L);

        findPostService.updateFindPost(1L, dto, member);

        assertEquals(updateFeatures, findPost.getFeatures());
        assertEquals(updateLocation, findPost.getLocation());
    }

    @Test
    void updateFindPost_위치변경X() {
        setFindPost();
        String updateFeatures = "특징 변경";
        boolean chip = true;

        FindPostReqDto dto = new FindPostReqDto(updateFeatures, Gender.M, true, chip,
                "경기도", 37.468873, 126.853229, LocalDateTime.now());
        when(findPostRepository.findById(anyLong())).thenReturn(Optional.ofNullable(findPost));
        when(member.getId()).thenReturn(1L);

        findPostService.updateFindPost(1L, dto, member);

        assertEquals(updateFeatures, findPost.getFeatures());
        assertEquals(chip, findPost.isChip());
    }


    private void setFindPost() {
        FindPostReqDto dto = new FindPostReqDto("특징", Gender.M, false, false,
                "경기도", 37.468873, 126.853229, LocalDateTime.now());
        findPost = FindPost.builder()
                .dto(dto)
                .writer(member)
                .point(PointParser.parsePoint(dto.getFindLatitude(), dto.getFindLongitude()))
                .build();
    }
}