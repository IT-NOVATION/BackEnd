package com.ItsTime.ItNovation.domain.movie.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewMovieInfoDto {

    private Long movieId;
    private String title;
    private String movieReleaseDate;
    private String movieImg;
    private String movieGenre;
    private String movieCountry;


    @Builder
    public ReviewMovieInfoDto(Long movieId, String title, String movieReleaseDate, String movieImg,
        String movieGenre, String movieCountry) {
        this.movieId = movieId;
        this.title = title;
        this.movieReleaseDate = movieReleaseDate;
        this.movieImg = movieImg;
        this.movieGenre = movieGenre;
        this.movieCountry = movieCountry;
    }


}
