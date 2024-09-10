package com.pesonal.FindDogPlz.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LostPostOutlineDto {
    private Long id;
    private String animalName;
    private String lostLocation;
    private LocalDateTime lostDate;
    private LocalDateTime createdDate;
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
