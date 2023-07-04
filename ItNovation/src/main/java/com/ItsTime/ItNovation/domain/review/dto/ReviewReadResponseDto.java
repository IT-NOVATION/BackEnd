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


    private ReviewInfoDto reviewInfoDto;
    private ReviewMovieInfoDto reviewMovieInfoDto;
    private ReviewUserInfoDto reviewUserInfoDto;


    @Builder
    public ReviewReadResponseDto(ReviewInfoDto reviewInfoDto, ReviewMovieInfoDto reviewMovieInfoDto,
        ReviewUserInfoDto reviewUserInfoDto) {
        this.reviewInfoDto = reviewInfoDto;
        this.reviewMovieInfoDto = reviewMovieInfoDto;
        this.reviewUserInfoDto = reviewUserInfoDto;
    }
}
