package com.ItsTime.ItNovation.config.auth.dto;


import com.ItsTime.ItNovation.domain.BaseTimeEntity;
import com.ItsTime.ItNovation.domain.user.Role;
import com.ItsTime.ItNovation.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
@Getter
@Builder
@Slf4j
public class OAuthAttributes extends BaseTimeEntity {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;


    public static OAuthAttributes of(String registrationId,String userNameAttributeName,Map<String,Object> attributes){
        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }else if ("kakao".equals(registrationId)) {
            return ofKakao("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        log.info(attributes.keySet().toString());
        log.info(attributes.get("email").toString());
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        log.info(attributes.keySet().toString());
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");
        log.info(response.keySet().toString());
        log.info(profile.keySet().toString());

        return OAuthAttributes.builder()
                .name((String) profile.get("nickname"))
                .email((String) response.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        log.info(attributes.keySet().toString());
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        log.info(response.keySet().toString());

        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .password("oauth")
                .role(Role.USER)
                .build();
    }
}
