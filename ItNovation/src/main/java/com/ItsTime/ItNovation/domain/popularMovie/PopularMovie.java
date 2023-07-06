package com.ItsTime.ItNovation.domain.popularMovie;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class PopularMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long movieId;

    private String title;

    private String movieImg;

    private Double popularity;

    @Builder
    PopularMovie(Long movieId,String title, String movieImg, Double popularity){
        this.movieId = movieId;
        this.title = title;
        this.movieImg = movieImg;
        this.popularity = popularity;
    }



}

