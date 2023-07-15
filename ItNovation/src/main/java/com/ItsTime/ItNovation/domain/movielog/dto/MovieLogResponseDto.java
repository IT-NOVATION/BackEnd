package com.ItsTime.ItNovation.domain.movielog.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MovieLogResponseDto {

    private  MovieLogUserInfoDto nowUser;
    private  List<MovieLogfollowersInfoDto> followers;
    private  List<MovieLogfollowingInfoDto> followings;
    private  List<MovieLogReviewInfoDto> reviews;
    private  List<MovieLogInterestedMovieInfoDto> interestedMovie;

    @Builder
    public MovieLogResponseDto(MovieLogUserInfoDto movieLogUserInfoDto, List<MovieLogfollowersInfoDto> movieLogfollowersInfoDtoList, List<MovieLogfollowingInfoDto> movieLogfollowingInfoDtoList, List<MovieLogReviewInfoDto> movieLogReviewInfoDtoList, List<MovieLogInterestedMovieInfoDto> movieLogInterestedMovieInfoDtoList) {
        this.nowUser = movieLogUserInfoDto;
        this.followers = movieLogfollowersInfoDtoList;
        this.followings = movieLogfollowingInfoDtoList;
        this.reviews = movieLogReviewInfoDtoList;
        this.interestedMovie = movieLogInterestedMovieInfoDtoList;
    }
}
