package com.pesonal.FindDogPlz.post.repository;

import com.pesonal.FindDogPlz.post.domain.FindPost;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pesonal.FindDogPlz.post.domain.QFindPost.findPost;

@Repository
@RequiredArgsConstructor
public class FindPostQueryRepositoryImpl implements FindPostQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<FindPost> searchAllByLastFindPostId(final Long lastFindPostId, final boolean close, final Pageable pageable) {
        List<FindPost> results = executeQuery(lastFindPostId, close, pageable);

        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private List<FindPost> executeQuery(final Long lastFindPostId, final boolean close, final Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(findPost)
                .innerJoin(findPost.writer)
                .where(
                        ltFindPostId(lastFindPostId),
                        findPost.completed.eq(close)
                )
                .orderBy(findPost.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetchJoin()
                .fetch();
    }

    private BooleanExpression ltFindPostId(final Long findPostId) {
        return findPostId != null ? findPost.id.lt(findPostId) : null;
    }
}
