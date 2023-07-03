package com.ItsTime.ItNovation.domain.movie.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieResponseDto {
    private Long movieId;
    private String movieTitle;
    private String movieImg;
    private Float starScore;

    @Builder
    public MovieResponseDto(Long movieId, String movieTitle, String movieImg, Float starScore) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieImg = movieImg;
        this.starScore = starScore;
    }
}
