package com.pesonal.FindDogPlz.timeLine.api;

import com.pesonal.FindDogPlz.global.auth.AuthMember;
import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.timeLine.application.TimeLineService;
import com.pesonal.FindDogPlz.timeLine.dto.TimeLineDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/timeline")
public class TimeLineController {
    private final TimeLineService timeLineService;

    @PostMapping
    public ResponseEntity<String> includeReportInTheTimeline(@RequestParam Long reportId, @RequestParam boolean finalLoc, @AuthMember Member lostPostWriter) {
        timeLineService.includeReportInTheTimeLine(reportId, finalLoc, lostPostWriter);
        return ResponseEntity.ok().body("완료");
    }

    @GetMapping
    public ResponseEntity<List<TimeLineDto>> getTimeLineForLostPost(@RequestParam Long lostPostId) {
        return ResponseEntity.ok(timeLineService.getTimeLineForLostPost(lostPostId));
    }
}
