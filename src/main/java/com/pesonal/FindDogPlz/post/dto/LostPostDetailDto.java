package com.pesonal.FindDogPlz.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LostPostDetailDto {
    private Long id;
    private Long writerId;
    private String writerName;

    private String animalName;
    private String features;
    private String gender;
    private boolean lead;
    private boolean chip;

    private String lostLocation;
    private String finalLocation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lostDate;

    @Builder
    public LostPostDetailDto (LostPost lostPost, Member writer) {
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
    }
}
