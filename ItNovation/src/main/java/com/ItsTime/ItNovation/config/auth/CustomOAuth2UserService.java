package com.ItsTime.ItNovation.config.auth;

import com.ItsTime.ItNovation.config.auth.dto.OAuthAttributes;
import com.ItsTime.ItNovation.config.auth.dto.SessionUser;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;
    // OAuth2UserService 인터페이스를 구현한 메서드, 이 메서드는 OAuth2 인증 서버에서 받아온 사용자 정보를 가공하여 반환
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate=new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId=userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName=userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        log.info(registrationId);
        log.info(userNameAttributeName);
        OAuthAttributes attributes=OAuthAttributes.of(registrationId,userNameAttributeName,oAuth2User.getAttributes());

        log.info("success to get attributes");
        log.info(attributes.getNameAttributeKey());
        User user=saveOrUpdate(attributes);
        httpSession.setAttribute("user",new SessionUser(user));
        log.info(registrationId);
        log.info(attributes.getAttributes().toString());
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),attributes.getAttributes(),attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user=userRepository.findByEmail(attributes.getEmail())
                .orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}
