package com.ItsTime.ItNovation.domain.movie.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewPostMovieInfoResponseDto {


    private Long movieId;
    private String title;
    private String movieImg;
    private String releaseDate;


    @Builder
    public ReviewPostMovieInfoResponseDto(Long movieId, String title, String movieImg, String releaseDate) {
        this.movieId = movieId;
        this.title = title;
        this.movieImg = movieImg;
        this.releaseDate = releaseDate;
    }
}
