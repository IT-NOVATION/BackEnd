package com.ItsTime.ItNovation.domain.movie.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MovieRecommendDto {
    private Long movieId;
    private String movieTitle;
    private String movieImg;
    private Float starScore;

    @Builder
    public MovieRecommendDto(Long movieId, String movieTitle, String movieImg, Float starScore) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieImg = movieImg;
        this.starScore = starScore;
    }





}
