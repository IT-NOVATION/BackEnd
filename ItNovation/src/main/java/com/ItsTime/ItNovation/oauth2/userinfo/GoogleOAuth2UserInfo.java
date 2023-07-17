package com.ItsTime.ItNovation.oauth2.userinfo;

import com.ItsTime.ItNovation.domain.user.Grade;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo{
    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("name");
    }


    @Override
    public String getBgImg() {
        return "default";
    }

    @Override
    public String getProfileImg() {
        return (String) attributes.get("picture");
    }

    @Override
    public String getEmail() { return (String) attributes.get("email");}
    @Override
    public String getGrade(){
        return Grade.STANDARD.name();}

}
