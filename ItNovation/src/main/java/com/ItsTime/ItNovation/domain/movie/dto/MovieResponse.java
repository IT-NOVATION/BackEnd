package com.ItsTime.ItNovation.domain.movie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieResponse {
    private Long movieId;
    private String movieTitle;
    private String movieImg;
    private Float starScore;

    public MovieResponse() {
    }

    public MovieResponse(Long movieId, String movieTitle, String movieImg, Float starScore) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieImg = movieImg;
        this.starScore = starScore;
    }
}

