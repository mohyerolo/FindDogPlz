package com.pesonal.FindDogPlz.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfoDto {
    @NotBlank
    private String grantType;

    @NotBlank
    private String accessToken;
}
