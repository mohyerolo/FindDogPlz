package com.pesonal.FindDogPlz.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @NotNull
    private Gender gender;

    @NotNull
    private boolean lead;

    @NotNull
    private boolean chip;

    @NotBlank
    private String lostLocation;

    @NotNull
    private Double lostLatitude;

    @NotNull
    private Double lostLongitude;

    @NotBlank
    private String finalLocation;

    @NotNull
    private Double finalLatitude;

    @NotNull
    private Double finalLongitude;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lostDate;

    public boolean isLostLocEqualFinalLoc() {
        return lostLocation.equals(finalLocation);
    }
}
