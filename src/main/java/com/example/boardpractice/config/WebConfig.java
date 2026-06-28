package com.example.boardpractice.config;

import com.example.boardpractice.common.utill.LoginUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginUserArgumentResolver loginUserArgumentResolver;

    public WebConfig(LoginUserArgumentResolver loginUserArgumentResolver) {
        this.loginUserArgumentResolver = loginUserArgumentResolver;
    }

    private static final String CORS_URL_PATTERN = "/**";
    private static final String CORS_URL = "http://127.0.0.1:5500";
    private static final String CORS_METHOD = "*";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping(CORS_URL_PATTERN)
                .allowedOrigins(CORS_URL)
                .allowedMethods(CORS_METHOD)
                .allowCredentials(true);

    }
    // 로그인 설정 스프링에 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
}