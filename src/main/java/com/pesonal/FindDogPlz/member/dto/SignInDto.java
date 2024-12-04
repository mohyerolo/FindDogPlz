package com.pesonal.FindDogPlz.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInDto {
    @NotBlank
    private String loginId;

    @NotBlank
    private String password;
}
