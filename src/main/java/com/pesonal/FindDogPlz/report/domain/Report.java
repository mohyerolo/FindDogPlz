package com.pesonal.FindDogPlz.report.domain;

import com.pesonal.FindDogPlz.global.common.BaseDateEntity;
import com.pesonal.FindDogPlz.global.util.PointParser;
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

    public void updateReport(final ReportReqDto dto) {
        updateLocation(dto);
        this.features = dto.getFeatures();
    }

    public void updateLocation(final ReportReqDto dto) {
        if (isLocationUpdated(dto.getFindLocation())) {
            this.point = PointParser.parsePoint(dto.getFindLatitude(), dto.getFindLongitude());;
            this.findLocation = dto.getFindLocation();
        }
    }

    private boolean isLocationUpdated(final String findLocation) {
        return !this.findLocation.equals(findLocation);
    }

    public void includedInTimeLine() {
        this.includedInTimeline = true;
    }

    public boolean isReporterDifferent(final Member member) {
        return writer.sameMember(member);
    }

    public void reflectReportLocationToPost() {
        lostPost.updateFinalLocation(findLocation, point);
    }
}
