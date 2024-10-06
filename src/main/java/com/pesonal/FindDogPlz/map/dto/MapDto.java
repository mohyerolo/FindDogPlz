package com.pesonal.FindDogPlz.map.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pesonal.FindDogPlz.post.domain.FindPost;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import com.pesonal.FindDogPlz.post.dto.PostType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MapDto {
    private Long id;

    private String location;
    private Double longitude;
    private Double latitude;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    private PostType postType;

    private Double dist;

    @Builder(builderMethodName = "lostPostBuilder", buildMethodName = "lostPostBuild")
    public MapDto(LostPost lostPost, Double dist) {
        this.id = lostPost.getId();
        this.location = lostPost.getLostLocation();
        this.longitude = lostPost.getLostPoint().getX();
        this.latitude = lostPost.getLostPoint().getY();
        this.createdDate = lostPost.getCreatedDate();
        this.postType = PostType.LOST;
        this.dist = dist;
    }

    @Builder(builderMethodName = "findPostBuilder", buildMethodName = "findPostBuild")
    public MapDto(FindPost findPost, Double dist) {
        this.id = findPost.getId();
        this.location = findPost.getLocation();
        this.longitude = findPost.getLocPoint().getX();
        this.latitude = findPost.getLocPoint().getY();
        this.createdDate = findPost.getCreatedDate();
        this.postType = PostType.FIND;
        this.dist = dist;
    }
}
