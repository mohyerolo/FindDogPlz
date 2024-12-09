package com.pesonal.FindDogPlz.post.application;

import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.global.util.PointParser;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.post.dto.LostPostDetailDto;
import com.pesonal.FindDogPlz.post.dto.LostPostOutlineDto;
import com.pesonal.FindDogPlz.post.dto.LostPostReqDto;
import com.pesonal.FindDogPlz.post.dto.LostPostUpdateDto;
import com.pesonal.FindDogPlz.post.repository.LostPostQueryRepository;
import com.pesonal.FindDogPlz.post.repository.LostPostRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LostPostService {
    private final LostPostRepository lostPostRepository;
    private final LostPostQueryRepository lostPostQueryRepository;

    @Transactional
    public void createLostPost(final LostPostReqDto dto, final Member member) {
        LostPost lostPost = createLostPostWithPoint(dto, member);
        lostPostRepository.save(lostPost);
    }

    private LostPost createLostPostWithPoint(final LostPostReqDto dto, final Member member) {
        Point lostPoint = parsePoint(dto.getLostLatitude(), dto.getLostLongitude());
        Point finalPoint = findFinalPoint(dto, lostPoint);

        return LostPost.builder()
                .dto(dto)
                .writer(member)
                .lostPoint(lostPoint)
                .finalPoint(finalPoint)
                .build();
    }

    private Point findFinalPoint(final LostPostReqDto dto, final Point lostPoint) {
        if (dto.isLostLocEqualFinalLoc()) {
            return lostPoint;
        }
        return parsePoint(dto.getFinalLatitude(), dto.getFinalLongitude());
    }

    @Transactional
    public Long updateLostPost(final Long id, final LostPostUpdateDto dto, final Member member) {
        LostPost lostPost = lostPostRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "수정하려는 공고를 찾을 수 없습니다."));
        validateWriter(lostPost, member);

        lostPost.updateLostPost(dto);
        return lostPost.getId();
    }

    public Slice<LostPostOutlineDto> getAllLostPost(final Long lastLostPostId, final boolean close, final int size) {
        PageRequest pageRequest = PageRequest.ofSize(size);
        return lostPostQueryRepository.searchByLastPostId(lastLostPostId, close, pageRequest);
    }

    public LostPostDetailDto getLostPostById(final Long id) {
        return lostPostRepository.findById(id)
                .map(lostPost -> LostPostDetailDto.builder()
                        .lostPost(lostPost)
                        .writer(lostPost.getWriter())
                        .build())
                .orElse(null);
    }

    @Transactional
    public void closeLostPost(final Long id, final Member member) {
        LostPost lostPost = lostPostRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "수정하려는 공고를 찾을 수 없습니다."));
        validateWriter(lostPost, member);
        lostPost.closePost();
    }

    private Point parsePoint(final Double latitude, final Double longitude) {
        return PointParser.parsePoint(latitude, longitude);
    }

    private void validateWriter(final LostPost lostPost, final Member member) {
        if (lostPost.isWriterDifferent(member)) {
            throw new AccessDeniedException("해당 작업이 가능한 사용자가 아닙니다.");
        }
    }
}
