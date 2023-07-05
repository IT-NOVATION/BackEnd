package com.ItsTime.ItNovation.domain.review.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ReviewReadRequestDto {




    private Long reviewId;


    @Builder
    public ReviewReadRequestDto(Long reviewId) {
        this.reviewId = reviewId;
    }
}
