package com.pesonal.FindDogPlz.global.config;

import com.pesonal.FindDogPlz.global.auth.JwtTokenProvider;
import com.pesonal.FindDogPlz.global.exception.CustomException;
import com.pesonal.FindDogPlz.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

            /*
              나중에 jwt 토큰값으로 인증하는 걸로 바꾸기? -> 지금 챗 연결은 JwtAuthenticationFilter를 거치고 있음.
              나중에 챗 주고 받을 때도 그러면 그냥 이걸로 해도 상관없을듯
             */
            String token = servletRequest.getHeader("Authorization");
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                throw new CustomException(ErrorCode.AUTHENTICATION_ERROR, "채팅은 인증된 사용자만 가능합니다.");
            }
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            if (authentication!= null) {
                attributes.put("sender", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                return true;
            } else {
                servletResponse.setStatus(401);
                return false;
            }
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
