package com.ItsTime.ItNovation.domain.singleMoviePage;

import com.ItsTime.ItNovation.domain.movie.dto.SingleMoviePageMovieInfoDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SingleMoviePageResponseDto {


    private List<SingleMoviePageReviewAndUserDto> reviewAndUserInfoList;

    private SingleMoviePageMovieInfoDto movie;


    @Builder
    public SingleMoviePageResponseDto(List<SingleMoviePageReviewAndUserDto> reviewAndUserInfoList, SingleMoviePageMovieInfoDto movie) {
        this.reviewAndUserInfoList = reviewAndUserInfoList;
        this.movie = movie;

    }


}
