package com.pesonal.FindDogPlz.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LostLocationUpdateDto {

    @NotBlank
    String lostLocation;

    @NotNull
    Double latitude;

    @NotNull
    Double longitude;
}
