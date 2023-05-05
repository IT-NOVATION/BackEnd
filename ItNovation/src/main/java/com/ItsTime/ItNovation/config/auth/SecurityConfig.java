package com.ItsTime.ItNovation.config.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    //HttpSecurity 객체를 사용하여 Spring Security의 인증 및 권한 부여 규칙을 정의
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                .formLogin().disable()
                .logout()
                .logoutSuccessUrl("/home")
                .and()
                .httpBasic().disable()
                .authorizeHttpRequests()
                    .requestMatchers("/", "/oauth2/**", "/login/**", "/css/**", "/images/**", "/js/**", "/console/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .oauth2Login()
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService);


        return http.build();
    }
}