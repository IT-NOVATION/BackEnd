package com.ItsTime.ItNovation.domain.movie.dto;

import com.ItsTime.ItNovation.domain.movie.Movie;
import lombok.Builder;

public class MovieSaveRequestDto {

    private String title;
    private Movie movie;



    @Builder
    public MovieSaveRequestDto(String title, Movie movie){
        this.title=title;
        this.movie=movie;
    }



}
