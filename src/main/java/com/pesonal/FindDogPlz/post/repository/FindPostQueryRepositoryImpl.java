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
    public Slice<FindPost> searchAllByLastFindPostId(Long lastFindPostId, Pageable pageable) {
        List<FindPost> results = jpaQueryFactory
                .selectFrom(findPost)
                .innerJoin(findPost.writer)
                .where(ltFindPostId(lastFindPostId))
                .orderBy(findPost.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetchJoin()
                .fetch();

        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private BooleanExpression ltFindPostId(Long findPostId) {
        return findPostId != null ? findPost.id.lt(findPostId) : null;
    }
}
