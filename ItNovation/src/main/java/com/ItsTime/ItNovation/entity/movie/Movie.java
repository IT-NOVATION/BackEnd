package com.ItsTime.ItNovation.entity.movie;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Movie {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;


    private String movieTitle;

    private String moviePosterUrl;

    @Builder
    public Movie(String title, String url) {
       this.movieTitle=title;
       this.moviePosterUrl=url;
    }




}
