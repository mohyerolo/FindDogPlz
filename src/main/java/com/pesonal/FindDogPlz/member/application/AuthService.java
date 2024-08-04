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
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
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
    public void signUp(SignUpDto dto) {
        validateDuplicateId(dto.getLoginId());
        Member member = Member.builder()
                .signUpDto(dto)
                .point(parsePoint(dto.getLatitude(), dto.getLongitude()))
                .pwEncoder(encoder)
                .build();
        memberRepository.save(member);
    }

    private void validateDuplicateId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new CustomException(ErrorCode.DUPLICATED_LOGIN_ID);
        }
    }

    private Point parsePoint(Double latitude, Double longitude) {
        try {
            return latitude != null && longitude != null ?
                    (Point) new WKTReader().read(String.format("POINT(%s %s)", longitude, latitude))
                    : null;
        } catch (ParseException e) {
            throw new CustomException(ErrorCode.POINT_PARSING_ERROR);
        }
    }

    public TokenInfoDto signIn(SignInDto dto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(dto.getLoginId(), dto.getPassword());
        Authentication authenticate = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        return jwtTokenProvider.generateToken(authenticate);
    }
}
