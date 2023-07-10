package com.ItsTime.ItNovation.domain.movielog.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MovieLogUserInfoDto {


    private final Long userId;
    private final String bgImg;
    private final String nickname;
    private final String introduction;
    private final Enum grade;
    private final String profileImg;
    @Builder
    public MovieLogUserInfoDto(Long userId, String bgImg, String nickname, String introduction, Enum grade, String profileImg) {
        this.userId = userId;
        this.bgImg = bgImg;
        this.nickname = nickname;
        this.introduction = introduction;
        this.grade = grade;
        this.profileImg = profileImg;
    }
}
