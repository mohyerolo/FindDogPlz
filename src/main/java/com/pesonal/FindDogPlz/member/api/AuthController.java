package com.pesonal.FindDogPlz.member.api;

import com.pesonal.FindDogPlz.member.application.AuthService;
import com.pesonal.FindDogPlz.member.dto.SignInDto;
import com.pesonal.FindDogPlz.member.dto.SignUpDto;
import com.pesonal.FindDogPlz.member.dto.TokenInfoDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpDto dto) {
        authService.signUp(dto);
        return ResponseEntity.ok().body("완료");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenInfoDto> signIn(@RequestBody @Valid SignInDto dto) {
        return ResponseEntity.ok(authService.signIn(dto));
    }
}
