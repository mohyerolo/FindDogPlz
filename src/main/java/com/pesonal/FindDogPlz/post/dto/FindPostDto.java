package com.pesonal.FindDogPlz.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pesonal.FindDogPlz.post.domain.FindPost;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPostDto {

    @NotNull
    private Long id;

    private Long writerId;
    private String writerName;

    @NotBlank
    private String features;

    @NotNull
    private boolean lead;

    @NotNull
    private boolean chip;

    @NotBlank
    private String findLocation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime findDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @Builder
    public FindPostDto(FindPost findPost) {
        this.id = findPost.getId();
        this.writerId = findPost.getWriter().getId();
        this.writerName = findPost.getWriter().getName();
        this.features = findPost.getFeatures();
        this.lead = findPost.isLead();
        this.chip = findPost.isChip();
        this.findLocation = findPost.getLocation();
        this.findDate = findPost.getFindDate();
        this.createdDate = findPost.getCreatedDate();
    }
}
