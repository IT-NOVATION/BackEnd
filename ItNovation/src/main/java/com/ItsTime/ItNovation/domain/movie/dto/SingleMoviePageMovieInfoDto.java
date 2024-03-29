package com.ItsTime.ItNovation.domain.movie.dto;


import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SingleMoviePageMovieInfoDto {


    private String movieImg;
    private String movieBgImg;
    private String title;
    private String movieGenre;
    private String movieReleasedDate;
    private Integer movieRunningTime;
    private List<String> movieActor;
    private String movieDetail;
    private MovieFeatureDto top3HasFeature;
    private Integer movieLikeCount;
    private String movieDirector;
    private String movieAge;
    private String movieCountry;
    private Float avgStarScore;


    @Builder
    public SingleMoviePageMovieInfoDto(String movieImg,String movieBgImg, String title, String movieGenre,
        String movieReleasedDate,
        Integer movieRunningTime, List<String> movieActor, String movieDetail,
        MovieFeatureDto top3HasFeature,
        Integer movieLikeCount, Float avgStarScore, String movieDirector, String movieAge, String movieCountry) {
        this.movieImg = movieImg;
        this.movieBgImg = movieBgImg;
        this.title = title;
        this.movieGenre = movieGenre;
        this.movieReleasedDate = movieReleasedDate;
        this.movieRunningTime = movieRunningTime;
        this.movieActor = movieActor;
        this.movieDetail = movieDetail;
        this.top3HasFeature = top3HasFeature;
        this.movieLikeCount = movieLikeCount;
        this.avgStarScore = avgStarScore;
        this.movieDirector = movieDirector;
        this.movieAge = movieAge;
        this.movieCountry = movieCountry;
    }
}
