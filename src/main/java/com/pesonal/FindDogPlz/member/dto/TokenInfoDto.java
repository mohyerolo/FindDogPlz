package com.pesonal.FindDogPlz.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfoDto {
    @NotNull
    private String grantType;

    @NotNull
    private String accessToken;
}
