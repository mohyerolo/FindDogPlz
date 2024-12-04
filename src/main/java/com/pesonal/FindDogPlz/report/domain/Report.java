package com.pesonal.FindDogPlz.report.domain;

import com.pesonal.FindDogPlz.global.common.BaseDateEntity;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.report.dto.ReportReqDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Report extends BaseDateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private LostPost lostPost;

    @NotNull
    private String features;

    @NotNull
    private String findLocation;

    @NotNull
    @Column(columnDefinition = "point")
    private Point point;

    @NotNull
    private boolean includedInTimeline;

    @Builder
    public Report(ReportReqDto dto, Member member, LostPost lostPost, Point point) {
        this.writer = member;
        this.lostPost = lostPost;
        this.features = dto.getFeatures();
        this.findLocation = dto.getFindLocation();
        this.point = point;
        this.includedInTimeline = false;
    }

    public void updateReport(ReportReqDto dto) {
        this.features = dto.getFeatures();
    }

    public void updateLocation(String location, Point point) {
        this.findLocation = location;
        this.point = point;
    }

    public void includedInTimeLine() {
        this.includedInTimeline = true;
    }
}
