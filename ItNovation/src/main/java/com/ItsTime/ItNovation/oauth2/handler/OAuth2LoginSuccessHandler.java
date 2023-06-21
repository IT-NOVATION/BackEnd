package com.ItsTime.ItNovation.oauth2.handler;

import com.ItsTime.ItNovation.domain.user.Role;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.oauth2.CustomOAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor

public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            if (oAuth2User.getRole() == Role.GUEST) {
                log.info("guest");

                String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
                String refreshToken = jwtService.createRefreshToken();
                log.info(accessToken);
                log.info(refreshToken);

                HttpSession session=request.getSession();
                session.setAttribute("accessToken",accessToken);
                session.setAttribute("refreshToken",refreshToken);
                jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);
                log.info(response.getHeader("Authorization"));


                userRepository.findByEmail(oAuth2User.getEmail())
                        .ifPresent(user->{
                            user.updateRefreshToken(refreshToken);
                            userRepository.saveAndFlush(user);
                        });



            } else {
                log.info("login before");
                loginSuccess(response, oAuth2User);
            }
        } catch (Exception e) {
            log.info("error");
            throw e;
        }




    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();
//        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
//        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }


}
