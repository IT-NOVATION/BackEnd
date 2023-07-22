package com.ItsTime.ItNovation.domain.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class LatestReviewResponseDto {

    private Long userId;
    private String profileImg;
    private Boolean isMyProfile;
    private Boolean isNowUserFollowThisUser;
    private String nickname;
    private String introduction;
    private List<LatestReviewDto> reviews;

    @Builder
    public LatestReviewResponseDto(Long userId, String profileImg, Boolean isMyProfile, Boolean loginUserPushedFollow, String nickname, String introduction, List<LatestReviewDto> reviews) {
        this.userId = userId;
        this.profileImg = profileImg;
        this.isMyProfile = isMyProfile;
        this.isNowUserFollowThisUser = loginUserPushedFollow;
        this.nickname = nickname;
        this.introduction = introduction;
        this.reviews = reviews;
    }
}
