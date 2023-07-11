package com.ItsTime.ItNovation.domain.movie.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MovieFeatureDto {

    private String top1Feature;
    private String top2Feature;
    private String top3Feature;



    @Builder

    public MovieFeatureDto(String top1Feature, String top2Feature, String top3Feature) {
        this.top1Feature = top1Feature;
        this.top2Feature = top2Feature;
        this.top3Feature = top3Feature;
    }
}
