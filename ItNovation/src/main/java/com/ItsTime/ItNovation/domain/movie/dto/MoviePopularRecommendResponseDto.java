package com.ItsTime.ItNovation.domain.movie.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MoviePopularRecommendResponseDto {
    private List<MoviePopularDto> popular;
    private List<MovieRecommendDto> recommended;

    @Builder
    public MoviePopularRecommendResponseDto(List<MoviePopularDto> popular, List<MovieRecommendDto> recommended) {
        this.popular = popular;
        this.recommended = recommended;
    }
}
