package com.ItsTime.ItNovation.domain.review.dto;


import com.ItsTime.ItNovation.domain.movie.dto.ReviewMovieInfoDto;
import com.ItsTime.ItNovation.domain.user.dto.ReviewLoginUserInfoDto;
import com.ItsTime.ItNovation.domain.user.dto.ReviewUserInfoDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ReviewReadResponseDto {


    private ReviewInfoDto review;
    private ReviewMovieInfoDto movie;
    private ReviewUserInfoDto user;
    private ReviewLoginUserInfoDto loginUser;


    @Builder
    public ReviewReadResponseDto(ReviewInfoDto review, ReviewMovieInfoDto movie,
        ReviewUserInfoDto user, ReviewLoginUserInfoDto loginUser) {
        this.review = review;
        this.movie = movie;
        this.user = user;
        this.loginUser = loginUser;
    }
}
