package com.ItsTime.ItNovation.domain.movie.dto;

import com.ItsTime.ItNovation.domain.movie.Movie;
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
