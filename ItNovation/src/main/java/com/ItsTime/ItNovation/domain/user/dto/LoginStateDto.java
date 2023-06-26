package com.ItsTime.ItNovation.domain.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginStateDto {

    private final Boolean loginState;
    private final Long userId;
    private final String nickname;
    private final String profileImg;


    @Builder
    public LoginStateDto(Boolean loginState, Long userId, String nickname, String profileImg) {
        this.loginState = loginState;
        this.userId = userId;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }
}
