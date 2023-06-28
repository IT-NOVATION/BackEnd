package com.ItsTime.ItNovation.oauth2.service;

import com.ItsTime.ItNovation.domain.user.SocialType;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.oauth2.CustomOAuth2User;
import com.ItsTime.ItNovation.oauth2.OAuthAttributes;
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
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    private static final String NAVER="naver";
    private static final String KAKAO="kakao";


    // OAuth2UserService 인터페이스를 구현한 메서드, 이 메서드는 OAuth2 인증 서버에서 받아온 사용자 정보를 가공하여 반환
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest,OAuth2User> delegate=new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId=userRequest.getClientRegistration().getRegistrationId();
        log.info(registrationId);
        SocialType socialType=getSocialType(registrationId);
        String userNameAttributeName=userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        log.info(userNameAttributeName);
        Map<String, Object> attributes = oAuth2User.getAttributes(); // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보들)

        // socialType에 따라 유저 정보를 통해 OAuthAttributes 객체 생성
        OAuthAttributes extractAttributes=OAuthAttributes.of(socialType,userNameAttributeName,attributes);

        User createdUser = getUser(extractAttributes);
        log.info(createdUser.getEmail());
        log.info(extractAttributes.getOAuth2UserInfo().getNickname());
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getRole().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdUser.getEmail(),
                createdUser.getRole()
        );
    }

    private User getUser(OAuthAttributes attributes) {
        User findUser = userRepository.findByEmail(attributes.getOAuth2UserInfo().getEmail())
                .orElse(null);
        if (findUser == null) {
            return saveUser(attributes);
        }
        return findUser;
    }

    private User saveUser(OAuthAttributes attributes) {
        User createdUser = attributes.toEntity(attributes.getOAuth2UserInfo());
        return userRepository.save(createdUser);
    }


    private SocialType getSocialType(String registrationId) {
        if (NAVER.equals(registrationId)) {
            return SocialType.NAVER;
        }
        if(KAKAO.equals(registrationId)) {
            return SocialType.KAKAO;
        }
        return SocialType.GOOGLE;
    }


}
