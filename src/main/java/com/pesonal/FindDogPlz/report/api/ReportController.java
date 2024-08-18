package com.pesonal.FindDogPlz.report.api;

import com.pesonal.FindDogPlz.global.auth.AuthMember;
import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.report.application.ReportService;
import com.pesonal.FindDogPlz.report.dto.ReportDto;
import com.pesonal.FindDogPlz.report.dto.ReportReqDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<String> createReport(@RequestParam Long postId, @RequestBody @Valid ReportReqDto dto, @AuthMember Member member) {
        if (member == null) throw new CustomException(ErrorCode.AUTHENTICATION_ERROR);
        reportService.createReport(postId, dto, member);
        return ResponseEntity.ok().body("완료");
    }

    @PutMapping
    public ResponseEntity<String> updateReport(@RequestParam Long postId, @RequestBody @Valid ReportReqDto dto, @AuthMember Member member) {
        if (member == null) throw new CustomException(ErrorCode.AUTHENTICATION_ERROR);
        reportService.updateReport(postId, dto, member);
        return ResponseEntity.ok().body("완료");
    }

    @GetMapping
    public ResponseEntity<Slice<ReportDto>> getAllReportForLostPost(@RequestParam Long postId, @RequestParam(required = false) Long lastReportId,
                                                                    @RequestParam(required = false, defaultValue = "9") int size) {
        return ResponseEntity.ok(reportService.getAllReportForLostPost(postId, lastReportId, size));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteReport(@RequestParam Long reportId, @AuthMember Member reportWriter) {
        if (reportWriter == null) throw new CustomException(ErrorCode.AUTHENTICATION_ERROR);
        reportService.deleteReport(reportId, reportWriter);
        return ResponseEntity.ok().body("완료");
    }

    @DeleteMapping("/exclude")
    public ResponseEntity<String> excludeReportByLostPostWriter(@RequestParam Long reportId, @AuthMember Member lostPostWriter) {
        if (lostPostWriter == null) throw new CustomException(ErrorCode.AUTHENTICATION_ERROR);
        reportService.excludeReportByLostPostWriter(reportId, lostPostWriter);
        return ResponseEntity.ok().body("완료");
    }
}
