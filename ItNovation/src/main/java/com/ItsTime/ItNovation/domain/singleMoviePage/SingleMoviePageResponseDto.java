package com.ItsTime.ItNovation.domain.singleMoviePage;

import com.ItsTime.ItNovation.domain.movie.dto.SingleMoviePageMovieInfoDto;
import com.ItsTime.ItNovation.domain.user.dto.SingleMoviePageLoginUserInfoDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SingleMoviePageResponseDto {


    private List<SingleMoviePageReviewAndUserDto> reviewAndUserInfoList;

    private SingleMoviePageMovieInfoDto movie;
    private SingleMoviePageLoginUserInfoDto loginUserInfoDto;


    @Builder
    public SingleMoviePageResponseDto(List<SingleMoviePageReviewAndUserDto> reviewAndUserInfoList, SingleMoviePageMovieInfoDto movie, SingleMoviePageLoginUserInfoDto loginUserInfoDto) {
        this.reviewAndUserInfoList = reviewAndUserInfoList;
        this.movie = movie;
        this.loginUserInfoDto = loginUserInfoDto;
    }


}
