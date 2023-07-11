package com.ItsTime.ItNovation.domain.star.dto;

import lombok.Getter;

@Getter
public class SingleStarEvaluateRequestDto {
    private Long userId;
    private Long movieId;
    private float starScore;
}
