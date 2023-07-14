package com.ItsTime.ItNovation.domain.star.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SingleStarEvaluateDto {

    private Long userId;
    private Long movieId;
    private float starScore;

    @Builder
    public SingleStarEvaluateDto(Long userId, Long movieId, float starScore) {
        this.userId = userId;
        this.movieId = movieId;
        this.starScore = starScore;
    }
}
