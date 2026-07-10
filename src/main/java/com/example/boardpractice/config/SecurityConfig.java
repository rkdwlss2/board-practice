package com.example.boardpractice.config;

import com.example.boardpractice.security.JsonLoginFilter;
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
                .authorizeHttpRequests(auth->auth
                        // permitAll 인증 없이 접근 가능한 경로
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // CORS preflight 요청
                        .requestMatchers(HttpMethod.POST, "/users/signup", "/users/login").permitAll() // 회원가입 로그인
                        .requestMatchers(HttpMethod.GET, "/boards/posts/**").permitAll() // 게시글 리스트 조회
                        .requestMatchers("/h2-console/**").permitAll() // H2 콘솔
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll() // 정적 리소스
                        //.requestMatchers("/boards/**").authenticated()
                        //.requestMatchers("/users/me/**").authenticated()

                        // user역할이 필요한 경우 인가
                        .requestMatchers(HttpMethod.POST,"/boards/posts").hasRole("USER") // 게시글 생성
                        .requestMatchers(HttpMethod.PUT,"/boards/posts/**").hasRole("USER") // 게시글 수정
                        .requestMatchers(HttpMethod.DELETE,"/boards/posts/**").hasRole("USER") // 게시글 삭제
                        .requestMatchers(HttpMethod.POST,"/boards/posts/{boardId}/comment").hasRole("USER") // 댓글 생성
                        .requestMatchers(HttpMethod.PUT,"/boards/posts/comments/**").hasRole("USER") // 댓글 수정
                        .requestMatchers(HttpMethod.DELETE,"/boards/posts/comment/**").hasRole("USER") // 댓글 삭제
                        .requestMatchers(HttpMethod.DELETE,"/users/me").hasRole("USER") // 회원탈퇴
                        .requestMatchers(HttpMethod.PUT,"/users/me/**").hasRole("USER") // 비밀번호 수정,닉네임 수정
                        .requestMatchers(HttpMethod.POST,"/boards/likes/**").hasRole("USER") // 좋아요
                        .requestMatchers(HttpMethod.DELETE,"/boards/likes/**").hasRole("USER") // 좋아요 취소
                        // 명시되지 않은 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/index.html")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }
}
