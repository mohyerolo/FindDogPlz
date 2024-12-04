package com.pesonal.FindDogPlz.report.application;

import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.global.util.PointParser;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.post.repository.LostPostRepository;
import com.pesonal.FindDogPlz.report.domain.Report;
import com.pesonal.FindDogPlz.report.dto.ReportDto;
import com.pesonal.FindDogPlz.report.dto.ReportReqDto;
import com.pesonal.FindDogPlz.report.repository.ReportQueryRepository;
import com.pesonal.FindDogPlz.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReportQueryRepository reportQueryRepository;

    private final LostPostRepository lostPostRepository;

    @Transactional
    public void createReport(final Long postId, final ReportReqDto dto, final Member member) {
        LostPost lostPost = lostPostRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.ACCEPTABLE_BUT_EMPTY, "제보하려던 공고가 없습니다."));

        Report report = createReport(lostPost, dto, member);
        reportRepository.save(report);
    }

    private Report createReport(final LostPost lostPost, final ReportReqDto dto, final Member member) {
        return Report.builder()
                .dto(dto)
                .point(parsePoint(dto.getFindLatitude(), dto.getFindLongitude()))
                .member(member)
                .lostPost(lostPost)
                .build();
    }

    @Transactional
    public void updateReport(final Long id, final ReportReqDto dto, final Member member) {
        Report report = findReport(id);
        validateWriter(report.getWriter(), member);

        report.updateReport(dto);
        updateFindLocation(report, dto);
    }

    private void updateFindLocation(final Report report, final ReportReqDto dto) {
        if (!report.getFindLocation().equals(dto.getFindLocation())) {
            Point findPoint = parsePoint(dto.getFindLatitude(), dto.getFindLongitude());
            report.updateLocation(dto.getFindLocation(), findPoint);
        }
    }

    public Slice<ReportDto> getAllReportForLostPost(final Long postId, final Long lastReportId, final int size) {
        PageRequest pageRequest = PageRequest.ofSize(size);
        return reportQueryRepository.searchAllReportByLostPostIdAndLastReportId(postId, lastReportId, pageRequest).map(ReportDto::new);
    }

    @Transactional
    public void deleteReport(final Long reportId, final Member reportWriter) {
        Report report = findReport(reportId);
        validateWriter(report.getWriter(), reportWriter);

        reportRepository.delete(report);
    }

    @Transactional
    public void excludeReportByLostPostWriter(final Long reportId, final Member lostPostWriter) {
        Report report = findReport(reportId);
        validateWriter(report.getLostPost().getWriter(), lostPostWriter);

        reportRepository.delete(report);
    }

    private Report findReport(final Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    private Point parsePoint(final Double latitude, final Double longitude) {
        return PointParser.parsePoint(latitude, longitude);
    }

    private void validateWriter(final Member writer, final Member member) {
        if (!writer.getId().equals(member.getId())) {
            throw new AccessDeniedException("해당 작업이 가능한 사용자가 아닙니다.");
        }
    }
}
