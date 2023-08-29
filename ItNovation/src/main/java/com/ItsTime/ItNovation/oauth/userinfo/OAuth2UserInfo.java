package com.ItsTime.ItNovation.oauth.userinfo;

import java.util.Map;

/**
 * 소셜 타입별로 유저 정보를 가지는 OAuth2UserInfo 추상 클래스
 */
public abstract class OAuth2UserInfo {
    //추상클래스를 상속받는 클래스에서만 사용할 수 있도록 protected 제어자를 사용
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getNickname();

    public abstract String getProfileImg();
    public abstract String getBgImg();

    public abstract String getEmail();
    public abstract String  getGrade();
}
