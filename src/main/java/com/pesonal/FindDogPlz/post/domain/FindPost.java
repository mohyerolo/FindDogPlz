package com.pesonal.FindDogPlz.post.domain;

import com.pesonal.FindDogPlz.global.common.BaseDateEntity;
import com.pesonal.FindDogPlz.global.common.Gender;
import com.pesonal.FindDogPlz.member.domain.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPost extends BaseDateEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

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
    private Point location_point;
}
