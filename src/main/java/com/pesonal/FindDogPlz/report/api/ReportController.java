package com.pesonal.FindDogPlz.report.api;

import com.pesonal.FindDogPlz.global.auth.AuthMember;
import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.report.application.ReportService;
import com.pesonal.FindDogPlz.report.dto.ReportReqDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
}
