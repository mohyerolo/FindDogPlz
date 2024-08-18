package com.pesonal.FindDogPlz.timeLine.application;

import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.report.domain.Report;
import com.pesonal.FindDogPlz.report.repository.ReportRepository;
import com.pesonal.FindDogPlz.timeLine.domain.TimeLine;
import com.pesonal.FindDogPlz.timeLine.dto.TimeLineDto;
import com.pesonal.FindDogPlz.timeLine.repository.TimeLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeLineService {
    private final TimeLineRepository timeLineRepository;
    private final ReportRepository reportRepository;

    @Transactional
    public void includeReportInTheTimeLine(Long reportId, boolean finalLoc, Member lostPostWriter) {
        Report report = findReport(reportId);
        validateMemberIsLostPostWriter(report.getLostPost(), lostPostWriter);

        timeLineRepository.save(createTimeLineFromReport(report));

        updateRelatedDetails(report, finalLoc);
    }

    private TimeLine createTimeLineFromReport(Report report) {
        return TimeLine.builder()
                .lostPost(report.getLostPost())
                .report(report)
                .reportedDate(report.getCreatedDate())
                .build();
    }

    private void updateRelatedDetails(Report report, boolean finalLoc) {
        if (finalLoc) {
            updateLostPostLocation(report);
        }
        report.includedInTimeLine();
    }

    private void updateLostPostLocation(Report report) {
        LostPost lostPost = report.getLostPost();
        lostPost.updateFinalLocation(report.getFindLocation(), report.getPoint());
    }

    private Report findReport(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    private void validateMemberIsLostPostWriter(LostPost lostPost, Member member) {
        if (!lostPost.getWriter().getId().equals(member.getId())) {
            throw new AccessDeniedException("해당 작업이 가능한 사용자가 아닙니다.");
        }
    }

    @Transactional
    public List<TimeLineDto> getTimeLineForLostPost(Long lostPostId) {
        List<TimeLine> timeLineList = timeLineRepository.findByLostPostIdOrderByReportedDateDesc(lostPostId);
        return timeLineList.stream().map(TimeLineDto::new).toList();
    }
}
