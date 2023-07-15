package com.ItsTime.ItNovation.domain.movielog.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MovieLogfollowersInfoDto {
    private final Long userId;
    private String nickname;
    private String profileImg;

    @Builder
    public MovieLogfollowersInfoDto(Long userId, String nickname, String profileImg) {
        this.userId = userId;
        this.nickname = nickname;
        this.profileImg = profileImg;
    }
}
