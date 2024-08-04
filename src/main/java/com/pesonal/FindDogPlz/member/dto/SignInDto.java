package com.pesonal.FindDogPlz.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInDto {
    @NotNull
    private String loginId;

    @NotNull
    private String password;
}
