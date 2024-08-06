package com.pesonal.FindDogPlz.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pesonal.FindDogPlz.global.common.Gender;
import jakarta.validation.Valid;
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
public class LostPostUpdateDto {

    @NotBlank
    private String animalName;

    @NotBlank
    private String features;

    @NotNull
    private Gender gender;

    @NotNull
    private boolean lead;

    @NotNull
    private boolean chip;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lostDate;

    @Valid
    LostLocationUpdateDto lostLocationUpdateDto;

    @Valid
    FinalLocationUpdateDto finalLocationUpdateDto;
}
