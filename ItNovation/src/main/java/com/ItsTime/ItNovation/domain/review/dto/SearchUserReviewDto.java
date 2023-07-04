package com.ItsTime.ItNovation.domain.review.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Builder.ObtainVia;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
