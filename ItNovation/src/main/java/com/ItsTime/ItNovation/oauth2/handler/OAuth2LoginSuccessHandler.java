package com.ItsTime.ItNovation.oauth2.handler;

import com.ItsTime.ItNovation.domain.user.Role;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.oauth2.CustomOAuth2User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
                String redirectUrl = "/next-signup";


                String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
                String refreshToken = jwtService.createRefreshToken();
                log.info(accessToken);
                log.info(refreshToken);

                //TODO: response 응답에서 authorization오나 확인해보기
                jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);
                log.info(oAuth2User.getEmail());

                userRepository.findByEmail(oAuth2User.getEmail())
                        .ifPresent(user->{
                            user.updateRefreshToken(refreshToken);
                            userRepository.saveAndFlush(user);
                        });
                //FIXME: 리다이렉트
                response.sendRedirect(redirectUrl);
                //TODO: 회원가입 추가 폼 입력 시 업데이트하는 컨트롤러, 서비스를 만들면 그 시점에 Role Update를 진행해야함
//                User findUser = userRepository.findByEmail(oAuth2User.getEmail())
//                               .orElseThrow(() -> new IllegalArgumentException("이메일에 해당하는 유저가 없습니다."));
//                findUser.authorizeUser();

            } else {
                log.info("login before");
                loginSuccess(response, oAuth2User);
            }
        } catch (Exception e) {
            log.info("error");
            throw e;
        }




    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        jwtService.sendAccessTokenAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
    }


}
