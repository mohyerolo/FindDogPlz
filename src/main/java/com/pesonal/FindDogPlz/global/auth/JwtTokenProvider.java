package com.pesonal.FindDogPlz.global.auth;

import com.pesonal.FindDogPlz.member.dto.TokenInfoDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final UserDetailsServiceImpl userDetailsService;
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;
    private final String GRANT_TYPE = "Bearer";

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey, UserDetailsServiceImpl userDetailsService) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userDetailsService = userDetailsService;
    }

    public TokenInfoDto generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        String token = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authorities)
                .setExpiration(expireDate)
                .signWith(key)
                .compact();
        return new TokenInfoDto(GRANT_TYPE, token);
    }

    public boolean validateToken(String token) throws JwtException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new JwtException("잘못된 타입의 토큰");
        } catch (ExpiredJwtException e) {
            throw new JwtException("만료된 토큰");
        } catch (UnsupportedJwtException e) {
            throw new JwtException("지원되지 않는 토큰");
        } catch (IllegalArgumentException e) {
            throw new JwtException("토큰이 존재하지 않음");
        }
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, accessToken, userDetails.getAuthorities());
    }
}
