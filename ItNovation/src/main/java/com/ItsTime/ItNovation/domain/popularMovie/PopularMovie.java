package com.ItsTime.ItNovation.domain.popularMovie;

import com.ItsTime.ItNovation.domain.movie.Movie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
    private Double popularity;
    private String title;
    private String movieImg;
    private String movieBgImg;
    private String movieDirector;
    private String movieDate;
    private String movieGenre;
    private String movieCountry;
    private String movieAudit;
    @Column(columnDefinition = "LONGTEXT")
    private String movieDetail;
    private Integer movieRunningTime;
    private Long real_movieId;
    private Long movieDbId;


    @Builder
    public PopularMovie(Double popularity, String title, String movieImg, String movieBgImg,
        String movieDirector, String movieDate, String movieGenre, String movieCountry,
        String movieAudit, String movieDetail, Integer movieRunningTime, Long real_movieId, Long movieDbId) {
        this.popularity = popularity;
        this.title = title;
        this.movieImg = movieImg;
        this.movieBgImg = movieBgImg;
        this.movieDirector = movieDirector;
        this.movieDate = movieDate;
        this.movieGenre = movieGenre;
        this.movieCountry = movieCountry;
        this.movieAudit = movieAudit;
        this.movieDetail = movieDetail;
        this.movieRunningTime = movieRunningTime;
        this.real_movieId = real_movieId;
        this.movieDbId = movieDbId;
    }




}

