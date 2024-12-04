package com.pesonal.FindDogPlz.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LostPostOutlineDto {
    @NotNull
    private Long id;

    @NotBlank
    private String animalName;

    @NotBlank
    private String lostLocation;

    @NotNull
    private LocalDateTime lostDate;
    @NotNull
    private LocalDateTime createdDate;

    @NotNull
    private boolean completed;

    @QueryProjection
    public LostPostOutlineDto(Long id, String animalName, String lostLocation, LocalDateTime lostDate, LocalDateTime createdDate, boolean completed) {
        this.id = id;
        this.animalName = animalName;
        this.lostLocation = lostLocation;
        this.lostDate = lostDate;
        this.createdDate = createdDate;
        this.completed = completed;
    }
}
