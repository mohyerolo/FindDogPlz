package com.pesonal.FindDogPlz.post.repository;

import com.pesonal.FindDogPlz.post.dto.LostPostOutlineDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface LostPostQueryRepository {
    Slice<LostPostOutlineDto> searchByLastPostId(Long lastLostPostId, boolean close, Pageable pageable);
}
