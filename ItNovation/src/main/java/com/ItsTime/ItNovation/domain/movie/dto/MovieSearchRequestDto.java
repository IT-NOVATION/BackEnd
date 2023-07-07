package com.ItsTime.ItNovation.domain.movie.dto;

import lombok.Getter;

@Getter
public class MovieSearchRequestDto {
    private String search;

    public MovieSearchRequestDto() {
    }

    public MovieSearchRequestDto(String search) {
        this.search = search;
    }
}
