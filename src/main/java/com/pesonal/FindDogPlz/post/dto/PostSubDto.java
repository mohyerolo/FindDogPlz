package com.pesonal.FindDogPlz.post.dto;

import com.pesonal.FindDogPlz.post.domain.FindPost;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostSubDto {
    private Long id;
    private PostType type;
    private String title;

    @Builder(builderMethodName = "lostPostBuilder", buildMethodName = "lostPostBuild")
    public PostSubDto(LostPost lostPost) {
        this.id = lostPost.getId();
        this.type = PostType.LOST;
        this.title = lostPost.getAnimalName();
    }

    @Builder(builderMethodName = "findPostBuilder", buildMethodName = "findPostBuild")
    public PostSubDto(FindPost findPost) {
        this.id = findPost.getId();
        this.type = PostType.FIND;
        this.title = findPost.getFeatures();
    }
}
