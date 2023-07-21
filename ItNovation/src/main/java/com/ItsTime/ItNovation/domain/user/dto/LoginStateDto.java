package com.ItsTime.ItNovation.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class
LoginStateDto {
    private Boolean loginState;
    private Long userId;
    private String nickname;
    private String profileImg;

    @Builder
    public LoginStateDto(Boolean loginState, Long userId, String nickname, String profileImg) {
        this.loginState = loginState;
        this.userId = userId;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }
}
