package com.ItsTime.ItNovation.domain.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewLoginUserInfoDto {

    private Boolean pushedFollow;
    private Boolean pushedReviewLike;

    @Builder
    public ReviewLoginUserInfoDto(Boolean pushedFollow, Boolean pushedReviewLike) {
        this.pushedFollow = pushedFollow;
        this.pushedReviewLike = pushedReviewLike;
    }
}
