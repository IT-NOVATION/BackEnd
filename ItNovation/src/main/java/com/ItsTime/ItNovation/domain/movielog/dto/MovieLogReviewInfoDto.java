package com.ItsTime.ItNovation.domain.movielog.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MovieLogReviewInfoDto {
    private final Long reviewId;
    private final String reviewTitle;
    private final Float star;
    private final String reviewMainText;
    private final String createdDate;
    private final int reviewLikeCount;
    private final int commentCount;
    private final Boolean hasSpoiler;
    private final MovieLogMovieofReviewsInfoDto movieofReview;

    @Builder
    public MovieLogReviewInfoDto(Long reviewId, String reviewTitle, Float star, String reviewMainText, String createdDate, Boolean hasSpoiler,int likeCount, int comments, MovieLogMovieofReviewsInfoDto movieLogMovieofReviewsInfoDtoList) {
        this.reviewId = reviewId;
        this.reviewTitle = reviewTitle;
        this.star = star;
        this.reviewMainText = reviewMainText;
        this.createdDate = createdDate;
        this.reviewLikeCount = likeCount;
        this.hasSpoiler = hasSpoiler;
        this.commentCount = comments;

        this.movieofReview = movieLogMovieofReviewsInfoDtoList;
    }
}
