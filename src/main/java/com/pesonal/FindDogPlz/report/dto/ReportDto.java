package com.pesonal.FindDogPlz.report.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pesonal.FindDogPlz.report.domain.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReportDto {
    private Long id;
    private Long writerId;
    private String writerName;

    private String features;
    private String findLocation;
    private Double findLatitude;
    private Double findLongitude;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @Builder
    public ReportDto(Report report) {
        this.id = report.getId();
        this.writerId = report.getWriter().getId();
        this.writerName = report.getWriter().getName();
        this.features = report.getFeatures();
        this.findLocation = report.getFindLocation();
        this.findLatitude = report.getPoint().getY();
        this.findLongitude = report.getPoint().getX();
        this.createdDate = report.getCreatedDate();
    }
}
