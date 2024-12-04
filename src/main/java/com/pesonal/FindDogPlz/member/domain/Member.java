package com.pesonal.FindDogPlz.member.domain;

import com.pesonal.FindDogPlz.global.common.BaseDateEntity;
import com.pesonal.FindDogPlz.member.dto.SignUpDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String loginId;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String phone;

    @ElementCollection
    private List<String> roles = new ArrayList<>();

    @Builder
    public Member(final SignUpDto signUpDto, final PasswordEncoder pwEncoder) {
        this.loginId = signUpDto.getLoginId();
        this.password = pwEncoder.encode(signUpDto.getPassword());
        this.name = signUpDto.getName();
        this.phone = signUpDto.getPhone();
        this.roles = Collections.singletonList("ROLE_USER");
    }
}
