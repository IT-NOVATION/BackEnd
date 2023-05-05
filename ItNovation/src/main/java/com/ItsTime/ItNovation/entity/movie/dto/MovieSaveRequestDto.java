package com.ItsTime.ItNovation.entity.movie.dto;

import com.ItsTime.ItNovation.entity.movie.Movie;
import com.ItsTime.ItNovation.service.movie.MovieRepoService;
import lombok.Builder;

public class MovieSaveRequestDto {

    private String title;
    private String posterUrl;



    @Builder
    public MovieSaveRequestDto(String title, String posterUrl){
        this.title=title;
        this.posterUrl=posterUrl;
    }

    public Movie toEntity(){
        return Movie.builder().
            title(title).
            url(posterUrl).
            build();
    }

}
