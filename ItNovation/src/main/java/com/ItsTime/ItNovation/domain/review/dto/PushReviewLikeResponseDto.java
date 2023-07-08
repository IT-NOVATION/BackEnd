package com.ItsTime.ItNovation.domain.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PushReviewLikeResponseDto {

    private Boolean isReviewLike;


    @Builder
    public PushReviewLikeResponseDto(Boolean isReviewLike) {
        this.isReviewLike = isReviewLike;
    }
}
