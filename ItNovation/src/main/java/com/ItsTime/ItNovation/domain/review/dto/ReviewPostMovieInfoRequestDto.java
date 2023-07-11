package com.ItsTime.ItNovation.domain.review.dto;


import lombok.Getter;

@Getter
public class ReviewPostMovieInfoRequestDto {

    private Long movieId;


    public ReviewPostMovieInfoRequestDto(Long movieId) {
        this.movieId = movieId;
    }
}
