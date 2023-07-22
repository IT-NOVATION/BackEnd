package com.ItsTime.ItNovation.domain.movieSearch.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MovieSearchResponseDto {
    private final int lastPage;
    private  final int firstPage;
    private final int nowPage;
    private final List<MovieSearchDto> moiveSearchDtoList;


    @Builder
    public MovieSearchResponseDto(int lastPage, int firstPage, int nowPage, List<MovieSearchDto> moiveSearchDtoList) {
        this.lastPage = lastPage;
        this.firstPage = firstPage;
        this.nowPage = nowPage;
        this.moiveSearchDtoList = moiveSearchDtoList;
    }
}
