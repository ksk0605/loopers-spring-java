package com.loopers.support.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final UserIdHeaderInterceptor userIdHeaderInterceptor;

    public WebConfig(UserIdHeaderInterceptor userIdHeaderInterceptor) {
        this.userIdHeaderInterceptor = userIdHeaderInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userIdHeaderInterceptor)
            .addPathPatterns("/api/v1/users/me")
            .addPathPatterns("/api/v1/points");
    }
}
