package com.pesonal.FindDogPlz.post.dto;

import com.pesonal.FindDogPlz.post.domain.FindPost;
import com.pesonal.FindDogPlz.post.domain.LostPost;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostSubDto {
    @NotNull
    private Long id;

    @NotNull
    private PostType type;

    @NotBlank
    private String title;

    public static PostSubDto fromLostPost(final LostPost lostPost) {
        return PostSubDto.builder()
                .id(lostPost.getId())
                .type(PostType.LOST)
                .title(lostPost.getAnimalName())
                .build();
    }

    public static PostSubDto fromFindPost(final FindPost findPost) {
        return PostSubDto.builder()
                .id(findPost.getId())
                .type(PostType.FIND)
                .title(findPost.getFeatures())
                .build();
    }
}
