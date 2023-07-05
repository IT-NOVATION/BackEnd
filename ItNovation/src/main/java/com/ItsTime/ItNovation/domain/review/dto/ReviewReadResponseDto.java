package com.ItsTime.ItNovation.domain.review.dto;


import com.ItsTime.ItNovation.domain.movie.dto.ReviewMovieInfoDto;
import com.ItsTime.ItNovation.domain.user.dto.ReviewUserInfoDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewReadResponseDto {


    private ReviewInfoDto review;
    private ReviewMovieInfoDto movie;
    private ReviewUserInfoDto user;


    @Builder
    public ReviewReadResponseDto(ReviewInfoDto review, ReviewMovieInfoDto movie,
        ReviewUserInfoDto user) {
        this.review = review;
        this.movie = movie;
        this.user = user;
    }
}
