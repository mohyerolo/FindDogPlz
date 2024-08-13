package com.pesonal.FindDogPlz.report.application;

import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.global.util.PointParser;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.post.repository.LostPostRepository;
import com.pesonal.FindDogPlz.report.domain.Report;
import com.pesonal.FindDogPlz.report.dto.ReportReqDto;
import com.pesonal.FindDogPlz.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final LostPostRepository lostPostRepository;

    @Transactional
    public void createReport(Long postId, ReportReqDto dto, Member member) {
        LostPost lostPost = lostPostRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.ACCEPTABLE_BUT_EMPTY, "제보하려던 공고가 없습니다."));

        Report report = Report.builder()
                .dto(dto)
                .point(parsePoint(dto.getFindLatitude(), dto.getFindLongitude()))
                .member(member)
                .lostPost(lostPost)
                .build();
        reportRepository.save(report);
    }

    @Transactional
    public void updateReport(Long id, ReportReqDto dto, Member member) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, "수정할 공고를 찾지 못했습니다."));
        validateWriter(report.getWriter(), member);

        report.updateReport(dto);
        updateFindLocation(report, dto);
    }

    private void updateFindLocation(Report report, ReportReqDto dto) {
        if (!report.getFindLocation().equals(dto.getFindLocation())) {
            Point findPoint = parsePoint(dto.getFindLatitude(), dto.getFindLongitude());
            report.updateLocation(dto.getFindLocation(), findPoint);
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
