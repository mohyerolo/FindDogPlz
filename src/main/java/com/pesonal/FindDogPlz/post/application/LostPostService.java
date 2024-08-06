package com.pesonal.FindDogPlz.post.application;

import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.global.util.PointParser;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.post.dto.FinalLocationUpdateDto;
import com.pesonal.FindDogPlz.post.dto.LostLocationUpdateDto;
import com.pesonal.FindDogPlz.post.dto.LostPostReqDto;
import com.pesonal.FindDogPlz.post.dto.LostPostUpdateDto;
import com.pesonal.FindDogPlz.post.repository.LostPostRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LostPostService {
    private final LostPostRepository lostPostRepository;

    @Transactional
    public void createLostPost(LostPostReqDto dto, Member member) {
        Point lostPoint = parsePoint(dto.getLostLatitude(), dto.getLostLongitude());
        Point finalPoint = dto.isLostLocEqualFinalLoc() ? lostPoint : parsePoint(dto.getFinalLatitude(), dto.getFinalLongitude());

        LostPost lostPost = LostPost.builder()
                .dto(dto)
                .writer(member)
                .lostPoint(lostPoint)
                .finalPoint(finalPoint)
                .build();
        lostPostRepository.save(lostPost);
    }

    @Transactional
    public Long updateLostPost(Long id, LostPostUpdateDto dto, Member member) {
        LostPost lostPost = lostPostRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "수정하려는 공고를 찾을 수 없습니다."));

        validateWriter(lostPost.getWriter(), member);

        updateLostLocation(lostPost, dto.getLostLocationUpdateDto());
        updateFinalLocation(lostPost, dto.getFinalLocationUpdateDto());

        lostPost.updatePostContent(dto);

        return lostPost.getId();
    }

    private void updateLostLocation(LostPost lostPost, LostLocationUpdateDto dto) {
        if (!lostPost.getLostLocation().equals(dto.getLostLocation())) {
            Point lostPoint = parsePoint(dto.getLatitude(), dto.getLongitude());
            lostPost.updateLostLocation(dto.getLostLocation(), lostPoint);
        }
    }

    private void updateFinalLocation(LostPost lostPost, FinalLocationUpdateDto dto) {
        if (!lostPost.getFinalLocation().equals(dto.getFinalLocation())) {
            Point finalPoint = parsePoint(dto.getLatitude(), dto.getLongitude());
            lostPost.updateFinalLocation(dto.getFinalLocation(), finalPoint);
        }
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
