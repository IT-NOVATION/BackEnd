package com.ItsTime.ItNovation.domain.user.dto;

import lombok.Builder;
import lombok.Getter;


@Getter
public class UserPushLikeDto {

    private Long userId;
    private Boolean isMyProfile;
    private String profileImg;
    private String nickname;
    private Boolean isLoginUserFollowed;


    @Builder
    public UserPushLikeDto(Long userId,Boolean isMyProfile, String profileImg, String nickname,
        Boolean isLoginUserFollowed) {
        this.userId = userId;
        this.isMyProfile = isMyProfile;
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.isLoginUserFollowed = isLoginUserFollowed;
    }
}
