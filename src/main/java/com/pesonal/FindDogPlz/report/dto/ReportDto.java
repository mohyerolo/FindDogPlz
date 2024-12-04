package com.pesonal.FindDogPlz.report.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pesonal.FindDogPlz.report.domain.Report;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReportDto {
    @NotNull
    private Long id;

    @NotNull
    private Long writerId;

    @NotBlank
    private String writerName;

    @NotBlank
    private String features;

    @NotBlank
    private String findLocation;

    @NotNull
    private Double findLatitude;

    @NotNull
    private Double findLongitude;

    @NotNull
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
