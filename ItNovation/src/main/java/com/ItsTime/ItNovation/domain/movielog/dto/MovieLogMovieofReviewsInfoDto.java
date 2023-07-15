package com.ItsTime.ItNovation.domain.movielog.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MovieLogMovieofReviewsInfoDto {


    private final Long movieId;
    private final String movieImg;
    @Builder
    public MovieLogMovieofReviewsInfoDto(Long movieId, String movieImg) {
        this.movieId = movieId;
        this.movieImg = movieImg;
    }
}
