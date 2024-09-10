package com.pesonal.FindDogPlz.post.repository;

import com.pesonal.FindDogPlz.post.domain.FindPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FindPostQueryRepository {
    Slice<FindPost> searchAllByLastFindPostId(Long lastFindPostId, boolean close, Pageable pageable);
}
