package com.loopers.support.web;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UserIdHeaderInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("X-USER-ID");
        if (userId == null || userId.isBlank()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "X-USER-ID 헤더가 필요합니다.");
            return false;
        }
        return true;
    }
}
