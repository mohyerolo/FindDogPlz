package com.pesonal.FindDogPlz.post.domain;

import com.pesonal.FindDogPlz.global.common.BaseDateEntity;
import com.pesonal.FindDogPlz.global.common.Gender;
import com.pesonal.FindDogPlz.global.util.PointParser;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.dto.FinalLocationUpdateDto;
import com.pesonal.FindDogPlz.post.dto.LostLocationUpdateDto;
import com.pesonal.FindDogPlz.post.dto.LostPostReqDto;
import com.pesonal.FindDogPlz.post.dto.LostPostUpdateDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member writer;

    @NotNull
    @Size(min = 1, max = 20)
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

    @NotNull
    private boolean completed;

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
        this.completed = false;
    }

    public void updateLostPost(final LostPostUpdateDto dto) {
        updateLostLocation(dto.getLostLocationUpdateDto());
        updateFinalLocation(dto.getFinalLocationUpdateDto());
        updatePostContent(dto);
    }

    public void updateFinalLocation(final String finalLocation, final Point finalPoint) {
        this.finalLocation = finalLocation;
        this.finalPoint = finalPoint;
    }

    private void updateLostLocation(final LostLocationUpdateDto dto) {
        if (isLostLocationUpdated(dto.getLostLocation())) {
            this.lostPoint = PointParser.parsePoint(dto.getLatitude(), dto.getLongitude());
            this.lostLocation = dto.getLostLocation();
        }
    }

    private boolean isLostLocationUpdated(final String lostLocation) {
        return !this.lostLocation.equals(lostLocation);
    }

    private void updateFinalLocation(final FinalLocationUpdateDto dto) {
        if (isFinalLocationUpdated(dto.getFinalLocation())) {
            this.finalPoint = PointParser.parsePoint(dto.getLatitude(), dto.getLongitude());
            this.finalLocation = dto.getFinalLocation();
        }
    }

    private boolean isFinalLocationUpdated(final String finalLocation) {
        return !this.finalLocation.equals(finalLocation);
    }

    private void updatePostContent(final LostPostUpdateDto dto) {
        this.animalName = dto.getAnimalName();
        this.features = dto.getFeatures();
        this.gender = dto.getGender();
        this.lead = dto.isLead();
        this.chip = dto.isChip();
        this.lostDate = dto.getLostDate();
    }

    public void closePost() {
        this.completed = true;
    }

    public boolean isWriterDifferent(final Member member) {
        return !writer.sameMember(member);
    }

}
