package com.ItsTime.ItNovation.domain.review.dto;

import com.ItsTime.ItNovation.domain.movie.dto.LatestReviewMovieResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LatestReviewDto {
    private Long reviewId;
    private String reviewTitle;
    private LatestReviewMovieResponseDto movie;

    @Builder
    public LatestReviewDto(Long reviewId, String reviewTitle, LatestReviewMovieResponseDto movie) {
        this.reviewId = reviewId;
        this.reviewTitle = reviewTitle;
        this.movie = movie;
    }
}
