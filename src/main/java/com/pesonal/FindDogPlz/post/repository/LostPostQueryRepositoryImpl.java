package com.pesonal.FindDogPlz.post.repository;

import com.pesonal.FindDogPlz.post.dto.LostPostOutlineDto;
import com.pesonal.FindDogPlz.post.dto.QLostPostOutlineDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pesonal.FindDogPlz.post.domain.QLostPost.lostPost;

@Repository
@RequiredArgsConstructor
public class LostPostQueryRepositoryImpl implements LostPostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Slice<LostPostOutlineDto> searchByLastPostId(final Long lastLostPostId, final boolean close, final Pageable pageable) {
        List<LostPostOutlineDto> results = executeQuery(lastLostPostId, close, pageable);

        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private List<LostPostOutlineDto> executeQuery(final Long lastLostPostId, final boolean close, final Pageable pageable) {
        return jpaQueryFactory
                .select(createQLostPost())
                .from(lostPost)
                .where(
                        ltLostPostId(lastLostPostId),
                        lostPost.completed.eq(close)
                )
                .orderBy(lostPost.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    private QLostPostOutlineDto createQLostPost() {
        return new QLostPostOutlineDto(lostPost.id, lostPost.animalName,
                lostPost.lostLocation, lostPost.lostDate, lostPost.createdDate, lostPost.completed);
    }

    private BooleanExpression ltLostPostId(Long lostPostId) {
        return lostPostId != null ? lostPost.id.lt(lostPostId) : null;
    }
}
