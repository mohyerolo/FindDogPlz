package com.pesonal.FindDogPlz.post.domain;

import com.pesonal.FindDogPlz.global.common.BaseDateEntity;
import com.pesonal.FindDogPlz.global.common.Gender;
import com.pesonal.FindDogPlz.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

public class LostPost extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

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
    private String location;

    @NotNull
    private Point locPoint;

    @NotNull
    private String finalLocation;

    @NotNull
    private Point finalLocPoint;

    @NotNull
    private LocalDateTime lostDate;
}
