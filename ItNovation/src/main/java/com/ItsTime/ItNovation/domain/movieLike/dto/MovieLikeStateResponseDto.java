package com.ItsTime.ItNovation.domain.movieLike.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MovieLikeStateResponseDto {
    private boolean isMovieLike;

    @Builder
    public MovieLikeStateResponseDto(boolean isMovieLike) {
        this.isMovieLike = isMovieLike;
    }
}
