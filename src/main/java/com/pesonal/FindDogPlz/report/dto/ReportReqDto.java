package com.pesonal.FindDogPlz.report.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportReqDto {

    @NotBlank
    private String features;

    @NotBlank
    private String findLocation;

    @NotNull
    private Double findLatitude;

    @NotNull
    private Double findLongitude;
}
