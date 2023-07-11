package com.ItsTime.ItNovation.domain.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class SingleMoviePageUserInfoDto {


    private Long userId;
    private String nickName;
    private String userProfileImg;

    @Builder
    public SingleMoviePageUserInfoDto(Long userId, String nickName, String userProfileImg) {
        this.userId = userId;
        this.nickName = nickName;
        this.userProfileImg = userProfileImg;
    }
}
