package com.pesonal.FindDogPlz.member.application;

import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.member.dto.SignUpDto;
import com.pesonal.FindDogPlz.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.io.ParseException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("회원가입- 아이디 중복 에러")
    @Transactional
    void signUp_duplicatedId() {
        SignUpDto dto = new SignUpDto("garamId", "password", "garam", "010-1111-1111");

        when(memberRepository.existsByLoginId("garamId")).thenReturn(true);
        assertThatThrownBy(() -> authService.signUp(dto))
                .isExactlyInstanceOf(CustomException.class).hasMessageContaining("아이디");
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_phone_regexError() {
        SignUpDto dto = new SignUpDto("garamId", "password", "garam", "010-1111-1111");

        when(memberRepository.existsByLoginId("garamId")).thenReturn(false);

        authService.signUp(dto);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

}