package com.ItsTime.ItNovation.oauth2.userinfo;

import com.ItsTime.ItNovation.domain.user.Grade;

import java.util.Map;

public abstract class OAuth2UserInfo {
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
