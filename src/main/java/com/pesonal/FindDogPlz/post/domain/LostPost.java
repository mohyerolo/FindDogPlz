package com.pesonal.FindDogPlz.post.domain;

import com.pesonal.FindDogPlz.global.common.BaseDateEntity;
import com.pesonal.FindDogPlz.global.common.Gender;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.dto.LostPostReqDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LostPost extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @NotNull
    @Column(length = 20)
    private String animalName;

    @NotNull
    private String features;

    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Gender gender;

    @NotNull
    private boolean lead;

    @NotNull
    private boolean chip;

    @NotNull
    private String lostLocation;

    @NotNull
    @Column(columnDefinition = "point")
    private Point lostPoint;

    @NotNull
    private String finalLocation;

    @NotNull
    @Column(columnDefinition = "point")
    private Point finalPoint;

    @NotNull
    private LocalDateTime lostDate;

    @Builder
    public LostPost(LostPostReqDto dto, Member writer, Point lostPoint, Point finalPoint) {
        this.writer = writer;
        this.animalName = dto.getAnimalName();
        this.features = dto.getFeatures();
        this.gender = dto.getGender();
        this.lead = dto.isLead();
        this.chip = dto.isChip();
        this.lostLocation = dto.getLostLocation();
        this.lostPoint = lostPoint;
        this.finalLocation = dto.getFinalLocation();
        this.finalPoint = finalPoint;
        this.lostDate = dto.getLostDate();
    }
}
