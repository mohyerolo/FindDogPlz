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
    public void createFindPost(final FindPostReqDto dto, final Member member) {
        Point findPoint = parsePoint(dto.getFindLatitude(), dto.getFindLongitude());

        FindPost findPost = FindPost.builder()
                .dto(dto)
                .writer(member)
                .point(findPoint)
                .build();
        findPostRepository.save(findPost);
    }

    @Transactional
    public void updateFindPost(final Long id, final FindPostReqDto dto, final Member member) {
        FindPost findPost = findPostRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "수정할 공고를 찾지 못했습니다."));
        validateWriter(findPost, member);

        findPost.updatePost(dto);
    }

    public Slice<FindPostDto> getAllFindPost(final Long lastFindPostId, final boolean close, final int size) {
        PageRequest pageRequest = PageRequest.ofSize(size);
        return findPostQueryRepository.searchAllByLastFindPostId(lastFindPostId, close, pageRequest).map(FindPostDto::new);
    }

    @Transactional
    public void closeFindPost(final Long id, final Member member) {
        FindPost findPost = findPostRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "수정할 공고를 찾지 못했습니다."));
        validateWriter(findPost, member);
        findPost.closePost();
    }

    private Point parsePoint(final Double latitude, final Double longitude) {
        return PointParser.parsePoint(latitude, longitude);
    }

    private void validateWriter(final FindPost findPost, final Member member) {
        if (!findPost.isWriterEqual(member)) {
            throw new AccessDeniedException("해당 작업이 가능한 사용자가 아닙니다.");
        }
    }
}
