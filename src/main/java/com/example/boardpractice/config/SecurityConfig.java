package com.example.boardpractice.config;

import com.example.boardpractice.security.JsonLoginFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 암호화 엔진 등록
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager,
            ObjectMapper objectMapper
    ) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .formLogin(form ->form.disable())
                .httpBasic(basic->basic.disable())
                .addFilterAt(
                        new JsonLoginFilter(authenticationManager, objectMapper),
                        UsernamePasswordAuthenticationFilter.class
                )
                .authorizeHttpRequests(auth -> auth
                        // CORS preflight 요청
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 1. 회원가입 및 로그인 (/api 붙은 경로와 안 붙은 경로 둘 다 허용)
                        .requestMatchers("/api/users/signup", "/api/users/login", "/users/signup", "/users/login").permitAll()

                        // 2. 게시글 목록/상세 조회
                        .requestMatchers(HttpMethod.GET, "/api/boards/posts/**", "/boards/posts/**").permitAll()

                        // 3. H2 콘솔 및 정적 리소스
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                        // 4. USER 권한 필요 경로
                        .requestMatchers(HttpMethod.POST, "/api/boards/posts", "/boards/posts").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/boards/posts/**", "/boards/posts/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/boards/posts/**", "/boards/posts/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/boards/posts/{boardId}/comment", "/boards/posts/{boardId}/comment").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/boards/posts/comments/**", "/boards/posts/comments/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/boards/posts/comment/**", "/boards/posts/comment/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/me", "/users/me").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/users/me/**", "/users/me/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/users/me/image", "/users/me/image").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/users/me/*/image", "/users/me/*/image").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/boards/likes/**", "/boards/likes/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/boards/likes/**", "/boards/likes/**").hasRole("USER")

                        // 5. 그 외 나머지 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpServletResponse.SC_OK);
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }
}
