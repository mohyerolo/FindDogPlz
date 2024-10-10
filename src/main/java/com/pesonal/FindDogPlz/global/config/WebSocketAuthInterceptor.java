package com.pesonal.FindDogPlz.global.config;

import com.pesonal.FindDogPlz.global.exception.CustomException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (!(request instanceof ServletServerHttpRequest)) return false;

        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        try {
            attributes.put("sender", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            return true;
        } catch (CustomException e) {
            servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            servletResponse.getWriter().write(e.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
