package com.ItsTime.ItNovation.domain.review.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewCountResponseDto {
    private Long reviewCount;

    @Builder
    public ReviewCountResponseDto(Long reviewCount) {
        this.reviewCount = reviewCount;
    }
}
