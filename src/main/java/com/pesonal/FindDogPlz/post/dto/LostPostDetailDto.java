package com.pesonal.FindDogPlz.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LostPostDetailDto {
    @NotNull
    private Long id;
    @NotNull
    private Long writerId;

    @NotBlank
    private String writerName;

    @NotBlank
    private String animalName;

    @NotBlank
    private String features;

    @NotBlank
    private String gender;

    @NotNull
    private boolean lead;

    @NotNull
    private boolean chip;

    @NotBlank
    private String lostLocation;
    @NotBlank
    private String finalLocation;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lostDate;

    @NotNull
    private boolean completed;

    @Builder
    public LostPostDetailDto(final LostPost lostPost, final Member writer) {
        this.id = lostPost.getId();
        this.writerId = writer.getId();
        this.writerName = writer.getName();
        this.animalName = lostPost.getAnimalName();
        this.features = lostPost.getFeatures();
        this.gender = lostPost.getGender().name();
        this.lead = lostPost.isLead();
        this.chip = lostPost.isChip();
        this.lostLocation = lostPost.getLostLocation();
        this.finalLocation = lostPost.getFinalLocation();
        this.lostDate = lostPost.getLostDate();
        this.completed = lostPost.isCompleted();
    }
}
