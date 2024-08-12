package com.pesonal.FindDogPlz.post.application;

import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.global.util.PointParser;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.FindPost;
import com.pesonal.FindDogPlz.post.dto.FindPostDto;
import com.pesonal.FindDogPlz.post.dto.FindPostReqDto;
import com.pesonal.FindDogPlz.post.repository.FindPostQueryRepository;
import com.pesonal.FindDogPlz.post.repository.FindPostRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FindPostService {
    private final FindPostRepository findPostRepository;
    private final FindPostQueryRepository findPostQueryRepository;

    @Transactional
    public void createFindPost(FindPostReqDto dto, Member member) {
        Point findPoint = parsePoint(dto.getFindLatitude(), dto.getFindLongitude());

        FindPost findPost = FindPost.builder()
                .dto(dto)
                .writer(member)
                .point(findPoint)
                .build();
        findPostRepository.save(findPost);
    }

    @Transactional
    public void updateFindPost(Long id, FindPostReqDto dto, Member member) {
        FindPost findPost = findPostRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "수정할 공고를 찾지 못했습니다."));
        validateWriter(findPost.getWriter(), member);

        findPost.updatePost(dto);
        updateFindLocation(findPost, dto);
    }

    private void updateFindLocation(FindPost findPost, FindPostReqDto dto) {
        if (!findPost.getLocation().equals(dto.getLocation())) {
            Point findPoint = parsePoint(dto.getFindLatitude(), dto.getFindLongitude());
            findPost.updateFindLocation(dto.getLocation(), findPoint);
        }
    }

    public Slice<FindPostDto> getAllFindPost(Long lastFindPostId, int size) {
        PageRequest pageRequest = PageRequest.ofSize(size);
        return findPostQueryRepository.searchAllByLastFindPostId(lastFindPostId, pageRequest).map(FindPostDto::new);
    }

    private Point parsePoint(Double latitude, Double longitude) {
        return PointParser.parsePoint(latitude, longitude);
    }

    private void validateWriter(Member writer, Member member) {
        if (!writer.getId().equals(member.getId())) {
            throw new AccessDeniedException("해당 작업이 가능한 사용자가 아닙니다.");
        }
    }
}
