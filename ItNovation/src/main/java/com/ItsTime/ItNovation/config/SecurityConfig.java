package com.ItsTime.ItNovation.config;

import com.ItsTime.ItNovation.config.jwt.JwtAuthenticationEntryPoint;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.config.jwt.filter.JwtAuthenticationProcessingFilter;
import com.ItsTime.ItNovation.config.jwt.filter.JwtExceptionFilter;
import com.ItsTime.ItNovation.config.jwt.service.JwtService;
import com.ItsTime.ItNovation.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.ItsTime.ItNovation.login.handler.LoginSuccessHandler;
import com.ItsTime.ItNovation.login.handler.LoginFailureHandler;
import com.ItsTime.ItNovation.login.service.LoginService;
import com.ItsTime.ItNovation.oauth2.handler.OAuth2LoginFailureHandler;
import com.ItsTime.ItNovation.oauth2.handler.OAuth2LoginSuccessHandler;
import com.ItsTime.ItNovation.oauth2.service.CustomOAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class SecurityConfig {
    private final LoginService loginService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAuthenticationEntryPoint entryPoint;
    private final JwtExceptionFilter jwtExceptionFilter;

    private static final List<RequestMatcher> specialUrlMatchers = Arrays.asList(
            new AntPathRequestMatcher("/api/v1/account/login/**"),
            new AntPathRequestMatcher("/api/v1/account/signup"),
            new AntPathRequestMatcher("/api/v1/user/profile"),
            new AntPathRequestMatcher("/api/v1/account/custom-logout"),
            new AntPathRequestMatcher("/oauth2/**"),
            new AntPathRequestMatcher("/api/v1/user/state/**"),

            // 댓글관련
            new AntPathRequestMatcher("/api/v1/comment/read/**"),

            // 비밀번호 찾기 관련
            new AntPathRequestMatcher("/api/v1/email/password-find/**"),

            // 영화 관련
            new AntPathRequestMatcher("/api/v1/movies/**"),
            // 무비서치 관련
            new AntPathRequestMatcher("/api/v1/movie-search/**"),
            // 무비로그 관련
            new AntPathRequestMatcher("/api/v1/movielog/**"),
            //무비토크관련
            new AntPathRequestMatcher("/api/v1/movie-talk/**"),

            // 리뷰읽기관련
            new AntPathRequestMatcher("/api/v1/review/info/**"),

            //리뷰작성관련
            new AntPathRequestMatcher("/api/v1/review/movie-info/**"),
            //검색관련
            new AntPathRequestMatcher("/api/v1/search/**"),

            //개인영화페이지관련
            new AntPathRequestMatcher("/api/v1/single/movie-page/**"),
            new AntPathRequestMatcher("/api/v1/single/movie/review-count/**"),

            //top 관련
            new AntPathRequestMatcher("/api/v1/top/**"),
            //swagger관련


            new AntPathRequestMatcher("/swagger-ui/**"),
            new AntPathRequestMatcher("/v3/api-docs/**")



            );
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(specialUrlMatchers.toArray(new RequestMatcher[0]));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(specialUrlMatchers.toArray(new RequestMatcher[0])).permitAll()
                .anyRequest().authenticated() //위의 지정된 주소 제외 모든 주소들은 인증된 사용자만 접근 가능하다

                .and()


                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(oAuth2LoginFailureHandler)
                .userInfoEndpoint().userService(customOAuth2UserService);

        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter, jwtAuthenticationProcessingFilter().getClass());
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), BasicAuthenticationFilter.class)
                .exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint))
        ;


        return http.build();
    }





    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(jwtService, userRepository,specialUrlMatchers);
    }

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    /**
     * UserDetailsService는 커스텀 LoginService로 등록
     * 또한, FormLogin과 동일하게 AuthenticationManager로는 ProviderManager 사용하고 구현체로는 DaoAuthenticationProvider
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    /**
     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, userRepository);
    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }


}
