package com.pesonal.FindDogPlz.timeLine.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pesonal.FindDogPlz.report.domain.Report;
import com.pesonal.FindDogPlz.timeLine.domain.TimeLine;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TimeLineDto {

    @NotNull
    private Long id;

    @NotNull
    private Long reportId;

    @NotBlank
    private String location;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reportedDate;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime includedDate;

    @Builder
    public TimeLineDto(TimeLine timeLine) {
        Report report = timeLine.getReport();
        this.id = timeLine.getId();
        this.reportId = report.getId();
        this.location = report.getFindLocation();
        this.longitude = report.getPoint().getX();
        this.latitude = report.getPoint().getY();
        this.reportedDate = timeLine.getReportedDate();
        this.includedDate = timeLine.getCreatedDate();
    }
}
