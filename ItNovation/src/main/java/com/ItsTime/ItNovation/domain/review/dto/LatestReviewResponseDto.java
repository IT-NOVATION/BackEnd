package com.ItsTime.ItNovation.domain.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LatestReviewResponseDto {

    private Long userId;
    private String profileImg;
    private String nickname;
    private String introduction;
    private Long reviewId;
    private String reviewTitle;
    private Long movieId;
    private String movieImg;

    @Builder
    public LatestReviewResponseDto(Long userId, String profileImg, String nickname, String introduction, Long reviewId, String reviewTitle, Long movieId, String movieImg) {
        this.userId = userId;
        this.profileImg = profileImg;
        this.nickname = nickname;
        this.introduction = introduction;
        this.reviewId = reviewId;
        this.reviewTitle = reviewTitle;
        this.movieId = movieId;
        this.movieImg = movieImg;
    }
}
