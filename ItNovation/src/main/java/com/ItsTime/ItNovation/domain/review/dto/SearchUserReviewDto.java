package com.ItsTime.ItNovation.domain.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SearchUserReviewDto {

    private Long reviewId;
    private String movieImg;
    private String reviewTitle;


    @Builder
    public SearchUserReviewDto(Long reviewId, String movieImg, String reviewTitle) {
        this.reviewId = reviewId;
        this.movieImg = movieImg;
        this.reviewTitle = reviewTitle;
    }
}
