package com.pesonal.FindDogPlz.post.domain;

import com.pesonal.FindDogPlz.global.common.BaseDateEntity;
import com.pesonal.FindDogPlz.global.common.Gender;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.dto.FindPostReqDto;
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
public class FindPost extends BaseDateEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

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
    private String location;

    @NotNull
    @Column(columnDefinition = "point")
    private Point locPoint;

    @NotNull
    private LocalDateTime findDate;

    @NotNull
    private boolean completed;

    @Builder
    public FindPost(FindPostReqDto dto, Member writer, Point point) {
        this.writer = writer;
        this.features = dto.getFeatures();
        this.gender = dto.getGender();
        this.lead = dto.isLead();
        this.chip = dto.isChip();
        this.location = dto.getLocation();
        this.locPoint = point;
        this.findDate = dto.getFindDate();
        this.completed = false;
    }

    public void updatePost(FindPostReqDto dto) {
        this.features = dto.getFeatures();
        this.gender = dto.getGender();
        this.lead = dto.isLead();
        this.chip = dto.isChip();
        this.findDate = dto.getFindDate();
    }

    public void updateFindLocation(String location, Point findPoint) {
        this.location = location;
        this.locPoint = findPoint;
    }

    public void closePost() {
        this.completed = true;
    }
}
