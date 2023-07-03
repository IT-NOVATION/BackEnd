package com.ItsTime.ItNovation.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserProfileDtoMe {
    private final String nickname;
    private final String introduction;
    private final String profileImg;
    private final String bgImg;
@Builder
    public UserProfileDtoMe(String nickname, String introduction, String profileImg, String bgImg) {
        this.nickname = nickname;
        this.introduction = introduction;
        this.profileImg = profileImg;
        this.bgImg = bgImg;
    }
}
