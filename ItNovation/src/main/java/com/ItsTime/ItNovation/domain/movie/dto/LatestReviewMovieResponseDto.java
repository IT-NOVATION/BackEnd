package com.ItsTime.ItNovation.domain.movie.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LatestReviewMovieResponseDto {
    private Long movieId;
    private String movieImg;

    @Builder
    public LatestReviewMovieResponseDto(Long movieId, String movieImg) {
        this.movieId = movieId;
        this.movieImg = movieImg;
    }
}
