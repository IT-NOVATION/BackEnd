package com.ItsTime.ItNovation.domain.movie.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TodayBestReviewMovieDto {


    private Long movieId;
    private String movieImg;

    @Builder
    public TodayBestReviewMovieDto(Long movieId, String movieImg) {
        this.movieId = movieId;
        this.movieImg = movieImg;
    }
}
