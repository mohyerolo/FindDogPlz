package com.pesonal.FindDogPlz.post.dto;

import com.pesonal.FindDogPlz.post.domain.FindPost;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostSubDto {
    private Long id;
    private PostType type;
    private String title;

    public static PostSubDto fromLostPost(LostPost lostPost) {
        return PostSubDto.builder()
                .id(lostPost.getId())
                .type(PostType.LOST)
                .title(lostPost.getAnimalName())
                .build();
    }

    public static PostSubDto fromFindPost(FindPost findPost) {
        return PostSubDto.builder()
                .id(findPost.getId())
                .type(PostType.FIND)
                .title(findPost.getFeatures())
                .build();
    }
}
