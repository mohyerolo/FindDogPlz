package com.pesonal.FindDogPlz.report.repository;

import com.pesonal.FindDogPlz.report.domain.Report;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.pesonal.FindDogPlz.report.domain.QReport.report;

@Repository
@RequiredArgsConstructor
public class ReportQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Slice<Report> searchAllReportByLostPostIdAndLastReportId(final Long postId, final Long lastReportId, final Pageable pageable) {
        List<Report> results = executeQuery(postId, lastReportId, pageable);

        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private List<Report> executeQuery(final Long postId, final Long lastReportId, final Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(report)
                .innerJoin(report.writer)
                .where(report.lostPost.id.eq(postId),
                        ltReportId(lastReportId)
                )
                .orderBy(report.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetchJoin()
                .fetch();
    }

    private BooleanExpression ltReportId(Long reportId) {
        return reportId != null ? report.id.lt(reportId) : null;
    }
}
