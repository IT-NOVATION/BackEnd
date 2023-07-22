package com.ItsTime.ItNovation.domain.review.dto;

import com.ItsTime.ItNovation.domain.movie.dto.FollowMovieResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TopFollowerReviewResponseDto {
    private Long reviewId;
    private String reviewTitle;
    private FollowMovieResponseDto movie;

    @Builder
    public TopFollowerReviewResponseDto(Long reviewId, String reviewTitle, FollowMovieResponseDto movie) {
        this.reviewId = reviewId;
        this.reviewTitle = reviewTitle;
        this.movie = movie;
    }
}
