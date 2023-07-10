package com.ItsTime.ItNovation.domain.movielog.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
public class MovieLogResponseDto {

    private final MovieLogUserInfoDto movieLogUserInfoDto;
    private final List<MovieLogfollowersInfoDto> movieLogfollowersInfoDtoList;
    private final List<MovieLogfollowingInfoDto> movieLogfollowingInfoDtoList;
    private final List<MovieLogReviewsInfoDto> movieLogReviewsInfoDtoList;
    private final List<MovieLogInterestedMovieInfoDto> movieLogInterestedMovieInfoDtoList;

    @Builder
    public MovieLogResponseDto(MovieLogUserInfoDto movieLogUserInfoDto, List<MovieLogfollowersInfoDto> movieLogfollowersInfoDtoList, List<MovieLogfollowingInfoDto> movieLogfollowingInfoDtoList, List<MovieLogReviewsInfoDto> movieLogReviewsInfoDtoList,List<MovieLogInterestedMovieInfoDto> movieLogInterestedMovieInfoDtoList) {
        this.movieLogUserInfoDto = movieLogUserInfoDto;
        this.movieLogfollowersInfoDtoList = movieLogfollowersInfoDtoList;
        this.movieLogfollowingInfoDtoList = movieLogfollowingInfoDtoList;
        this.movieLogReviewsInfoDtoList = movieLogReviewsInfoDtoList;
        this.movieLogInterestedMovieInfoDtoList = movieLogInterestedMovieInfoDtoList;
    }
}
