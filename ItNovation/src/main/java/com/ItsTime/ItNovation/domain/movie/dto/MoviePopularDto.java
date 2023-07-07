package com.ItsTime.ItNovation.domain.movie.dto;

import lombok.*;

@Getter
public class MoviePopularDto {
    private Long movieId;
    private String movieTitle;
    private String movieImg;

    private float starScore;

    private int popularity;

    @Builder
    public MoviePopularDto(Long movieId, String movieTitle, String movieImg, int popularity, float starScore) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieImg = movieImg;
        this.popularity = popularity;
        this.starScore = starScore;
    }
}
