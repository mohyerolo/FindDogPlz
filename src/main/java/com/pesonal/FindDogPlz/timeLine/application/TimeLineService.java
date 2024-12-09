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
    public void includeReportInTheTimeLine(final Long reportId, final boolean finalLoc, final Member lostPostWriter) {
        Report report = findReport(reportId);
        validateMemberIsLostPostWriter(report.getLostPost(), lostPostWriter);

        timeLineRepository.save(createTimeLineFromReport(report));

        updateRelatedDetails(report, finalLoc);
    }

    private TimeLine createTimeLineFromReport(final Report report) {
        return TimeLine.builder()
                .lostPost(report.getLostPost())
                .report(report)
                .reportedDate(report.getCreatedDate())
                .build();
    }

    private void updateRelatedDetails(final Report report, final boolean finalLoc) {
        if (finalLoc) {
            report.reflectReportLocationToPost();
        }
        report.includedInTimeLine();
    }

    private Report findReport(final Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    private void validateMemberIsLostPostWriter(final LostPost lostPost, final Member member) {
        if (lostPost.isWriterDifferent(member)) {
            throw new AccessDeniedException("해당 작업이 가능한 사용자가 아닙니다.");
        }
    }

    @Transactional
    public List<TimeLineDto> getTimeLineForLostPost(final Long lostPostId) {
        List<TimeLine> timeLineList = timeLineRepository.findByLostPostIdOrderByReportedDateDesc(lostPostId);
        return timeLineList.stream().map(TimeLineDto::new).toList();
    }
}
