package com.ItsTime.ItNovation.domain.movieSearch.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MovieSearchDto {
    private final Long movieId;
    private final String movieImg;
    private final String movieTitle;
    private final Float starScore;
    private final int reviewCount;

    @Builder
    public MovieSearchDto(Long movieId, String movieImg, String movieTitle, Float starScore, int reviewCount) {
        this.movieId = movieId;
        this.movieImg = movieImg;
        this.movieTitle = movieTitle;
        this.starScore = starScore;
        this.reviewCount = reviewCount;
    }
}
