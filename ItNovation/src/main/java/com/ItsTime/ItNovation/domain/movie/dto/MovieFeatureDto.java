package com.ItsTime.ItNovation.domain.movie.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MovieFeatureDto {

    private List<String> topKeywordList;

    @Builder
    public MovieFeatureDto(List<String> topKeywordList) {
        this.topKeywordList = topKeywordList;
    }
}
