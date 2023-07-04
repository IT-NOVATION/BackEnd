package com.ItsTime.ItNovation.oauth2;


import com.ItsTime.ItNovation.domain.BaseTimeEntity;
import com.ItsTime.ItNovation.domain.user.Grade;
import com.ItsTime.ItNovation.domain.user.Role;
import com.ItsTime.ItNovation.domain.user.SocialType;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.oauth2.userinfo.GoogleOAuth2UserInfo;
import com.ItsTime.ItNovation.oauth2.userinfo.KakaoOAuth2UserInfo;
import com.ItsTime.ItNovation.oauth2.userinfo.NaverOAuth2UserInfo;
import com.ItsTime.ItNovation.oauth2.userinfo.OAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;

@Getter
@Slf4j
public class OAuthAttributes extends BaseTimeEntity {

    private String nameAttributeKey;
    private OAuth2UserInfo oAuth2UserInfo;
    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName, Map<String,Object> attributes){
        log.info("OAuthAttributes 객체 생성");
        if (socialType==socialType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }else if (socialType==socialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    /**
     * of메소드로 OAuthAttributes 객체가 생성되어, 유저 정보들이 담긴 OAuth2UserInfo가 소셜 타입별로 주입된 상태
     * OAuth2UserInfo에서 socialId(식별값), nickname, imageUrl을 가져와서 build
     * email에는 UUID로 중복 없는 랜덤 값 생성
     * role은 GUEST로 설정
     */
    public User toEntity(OAuth2UserInfo oAuth2UserInfo) {
        return User.builder()
                .email(oAuth2UserInfo.getEmail())
                .nickname(oAuth2UserInfo.getNickname())
                .bgImg(oAuth2UserInfo.getBgImg())
                .profileImg(oAuth2UserInfo.getProfileImg())
                .role(Role.GUEST)
                .grade(Grade.getDefault().getValue())
                .build();
    }
}
