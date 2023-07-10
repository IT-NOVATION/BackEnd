package com.ItsTime.ItNovation.domain.movielog.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MovieLogReviewsInfoDto {
    private final Long reviewId;
    private final String reviewTitle;
    private final Float star;
    private final String reviewMainText;
    private final String createdDate;
    private final int likeCount;
    private final int comments;
    private final MovieLogMovieofReviewsInfoDto movieLogMovieofReviewsInfoDtoList;

    @Builder
    public MovieLogReviewsInfoDto(Long reviewId, String reviewTitle, Float star, String reviewMainText, String createdDate, int likeCount, int comments, MovieLogMovieofReviewsInfoDto movieLogMovieofReviewsInfoDtoList) {
        this.reviewId = reviewId;
        this.reviewTitle = reviewTitle;
        this.star = star;
        this.reviewMainText = reviewMainText;
        this.createdDate = createdDate;
        this.likeCount = likeCount;
        this.comments = comments;
        this.movieLogMovieofReviewsInfoDtoList = movieLogMovieofReviewsInfoDtoList;
    }
}
