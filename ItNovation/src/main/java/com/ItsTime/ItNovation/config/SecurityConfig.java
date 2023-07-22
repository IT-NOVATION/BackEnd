package com.ItsTime.ItNovation.config;

import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.jwt.filter.JwtAuthenticationProcessingFilter;
import com.ItsTime.ItNovation.jwt.filter.JwtExceptionFilter;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.ItsTime.ItNovation.login.handler.CustomLogoutSuccessHandler;
import com.ItsTime.ItNovation.login.handler.LoginSuccessHandler;
import com.ItsTime.ItNovation.login.handler.LoginFailureHandler;
import com.ItsTime.ItNovation.login.service.LoginService;
import com.ItsTime.ItNovation.oauth2.handler.OAuth2LoginFailureHandler;
import com.ItsTime.ItNovation.oauth2.handler.OAuth2LoginSuccessHandler;
import com.ItsTime.ItNovation.oauth2.service.CustomOAuth2UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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


    List<RequestMatcher> specialUrlMatchers = Arrays.asList(

            // CustomJsonUsernamePasswordAuthenticationFilter 가 토큰 검증 이후의 필터이기에 여기에 등록
            new AntPathRequestMatcher("/login/**"),
            new AntPathRequestMatcher("/signup"),
            new AntPathRequestMatcher("/userProfile"),
            new AntPathRequestMatcher("/test/**"),
            new AntPathRequestMatcher("/oauth2/**"),
            new AntPathRequestMatcher("/loginState/**"),
            new AntPathRequestMatcher("/movies/**"),
            new AntPathRequestMatcher("/search/**"),
            new AntPathRequestMatcher("/today/**"),
            new AntPathRequestMatcher("/single/moviePage/**"),
            new AntPathRequestMatcher("/review/Info/**"),
            new AntPathRequestMatcher("/top/**"),
            new AntPathRequestMatcher("/single/movie/reviewCount/**"),
            new AntPathRequestMatcher("/custom-logout"),
            new AntPathRequestMatcher("/movielog/**"),
            new AntPathRequestMatcher("/movie-search/**")





            );


    //HttpSecurity 객체를 사용하여 Spring Security의 인증 및 권한 부여 규칙을 정의
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //== URL별 권한 관리 옵션==//
                .authorizeHttpRequests()

                .requestMatchers("/oauth2/**","/css/**", "/images/**", "/test/**","/js/**").permitAll()

                //TODO: 토큰 검증 필요없는 것은 위의 specialUrlMatchers 랑 아래 permitAll 에 등록 필수
//                .requestMatchers("/signup","/userProfile","/movies","/review", "/review/Info", "today/**","/loginState").permitAll()
                .requestMatchers(specialUrlMatchers.toArray(new RequestMatcher[0])).permitAll()


                .requestMatchers("/userProfile/me").authenticated() //userProfile과 충돌나지 않게 별도로 설정
                .anyRequest().authenticated() //위의 지정된 주소 제외 모든 주소들은 인증된 사용자만 접근 가능하다

                .and()

                //== 소셜 로그인 설정 ==//
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(oAuth2LoginFailureHandler)
                .userInfoEndpoint().userService(customOAuth2UserService);


        //TODO: 필터 순서
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(new SecurityFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtExceptionFilter(), jwtAuthenticationProcessingFilter().getClass());


        return http.build();
    }

    @Bean
    public JwtExceptionFilter jwtExceptionFilter() {
        JwtExceptionFilter jwtExceptionFilter = new JwtExceptionFilter();
        return jwtExceptionFilter;
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository,specialUrlMatchers);
        return jwtAuthenticationFilter;
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