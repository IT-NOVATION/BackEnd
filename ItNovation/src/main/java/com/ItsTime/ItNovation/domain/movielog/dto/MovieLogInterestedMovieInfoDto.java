package com.ItsTime.ItNovation.domain.movielog.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MovieLogInterestedMovieInfoDto {
    private Long movieId;
    private String movieImg;
    private Float star;
    private String title;

    private Boolean hasReviewdByLoginedUser;
    private Long reviewId;

    @Builder
    public MovieLogInterestedMovieInfoDto(Long movieId, String movieImg, Float star, String title, Boolean hasReviewdByLoginedUser,Long reviewId) {
        this.movieId = movieId;
        this.movieImg = movieImg;
        this.star = star;
        this.title = title;
        this.hasReviewdByLoginedUser = hasReviewdByLoginedUser;
        this.reviewId = reviewId;
    }
}
