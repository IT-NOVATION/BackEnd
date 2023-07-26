package com.ItsTime.ItNovation.domain.movie.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MovieSearchResponseDto {
    private List<MoviePopularDto> movieSearchResult;
    private int totalSize;

    @Builder
    public MovieSearchResponseDto(List<MoviePopularDto> movieSearchResult, int totalSize) {
        this.movieSearchResult = movieSearchResult;
        this.totalSize = totalSize;
    }
}
