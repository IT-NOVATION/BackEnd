package com.ItsTime.ItNovation.domain.singleMoviePage;

import com.ItsTime.ItNovation.domain.review.dto.SingleMoviePageReviewInfoDto;
import com.ItsTime.ItNovation.domain.user.dto.SingleMoviePageUserInfoDto;
import lombok.Builder;
import lombok.Getter;


@Getter
public class SingleMoviePageReviewAndUserDto {

    private SingleMoviePageUserInfoDto user;
    private SingleMoviePageReviewInfoDto review;


    @Builder
    public SingleMoviePageReviewAndUserDto(SingleMoviePageUserInfoDto user, SingleMoviePageReviewInfoDto review) {
        this.user = user;
        this.review = review;
    }
}
