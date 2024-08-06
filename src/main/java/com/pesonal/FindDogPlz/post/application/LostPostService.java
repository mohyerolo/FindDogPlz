package com.pesonal.FindDogPlz.post.application;

import com.pesonal.FindDogPlz.global.util.PointParser;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.post.dto.LostPostReqDto;
import com.pesonal.FindDogPlz.post.repository.LostPostRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LostPostService {
    private final LostPostRepository lostPostRepository;

    @Transactional
    public void createLostPost(LostPostReqDto dto, Member member) {
        Point lostPoint = PointParser.parsePoint(dto.getLostLatitude(), dto.getLostLongitude());
        Point finalPoint = dto.isLostLocEqualFinalLoc() ? lostPoint : PointParser.parsePoint(dto.getFinalLatitude(), dto.getFinalLongitude());

        LostPost lostPost = LostPost.builder()
                .dto(dto)
                .writer(member)
                .lostPoint(lostPoint)
                .finalPoint(finalPoint)
                .build();
        lostPostRepository.save(lostPost);
    }


}
