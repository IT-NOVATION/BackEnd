package com.ItsTime.ItNovation.domain.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class SingleMoviePageLoginUserInfoDto {

    private Float reviewStar;
    private Float movieStar;
    private Boolean pushedMovieLike;


    @Builder
    public SingleMoviePageLoginUserInfoDto(Float reviewStar,Float movieStar, Boolean pushedMovieLike) {
        this.reviewStar = reviewStar;
        this.movieStar = movieStar;
        this.pushedMovieLike=pushedMovieLike;
    }
}
