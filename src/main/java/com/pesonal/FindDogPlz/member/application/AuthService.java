package com.pesonal.FindDogPlz.member.application;

import com.pesonal.FindDogPlz.global.auth.JwtTokenProvider;
import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import com.pesonal.FindDogPlz.member.domain.Member;
import com.pesonal.FindDogPlz.member.dto.SignInDto;
import com.pesonal.FindDogPlz.member.dto.SignUpDto;
import com.pesonal.FindDogPlz.member.dto.TokenInfoDto;
import com.pesonal.FindDogPlz.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder encoder;

    @Transactional
    public void signUp(final SignUpDto dto) {
        validateDuplicateId(dto.getLoginId());
        Member member = Member.builder()
                .signUpDto(dto)
                .pwEncoder(encoder)
                .build();
        memberRepository.save(member);
    }

    private void validateDuplicateId(final String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new CustomException(ErrorCode.DUPLICATED_LOGIN_ID);
        }
    }

    public TokenInfoDto signIn(final SignInDto dto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getLoginId(), dto.getPassword());
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authenticate);
    }
}
