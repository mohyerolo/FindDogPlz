package com.pesonal.FindDogPlz.post.dto;

import com.pesonal.FindDogPlz.global.common.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LostPostReqDto {

    @NotBlank
    private String animalName;

    @NotBlank
    private String features;

    @NotBlank
    private Gender gender;

    @NotNull
    private boolean lead;

    @NotNull
    private boolean chip;

    @NotBlank
    private String location;

    @NotNull
    private Double LostLatitude;

    @NotNull
    private Double LostLongitude;

    @NotBlank
    private String finalLocation;

    @NotNull
    private Double finalLatitude;

    @NotNull
    private Double finalLongitude;

    @NotNull
    private LocalDateTime lostDate;
}
