package com.ItsTime.ItNovation.domain.movie;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Movie {
//[adult, backdrop_path, id, original_language, overview, popularity, release_date, title,  vote_average, vote_count]

//   "movie_id" bigint [pk, not null, increment]
//   "movie_name" VARCHAR(255)
//  "movie_img" VARCHAR(255)
//  "movie_bgImg" VARCHAR(255)
//  "movie_actor" varchar(255)
//  "movie_director" varchar(255)
//  "movie_date" DATE
//  "movie_genre" VARCHAR(255)
//  "movie_country" VARCHAR(255)
//  "movie_detail" text
//  "movie_running_time" INT

// 장르 러닝타임 감독 배우 줄거리 - 한국어(요청 url 변경)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String movieImg;
    private String movieBgImg;
    private String movieActor;
    private String movieDirector;
    private String movieDate;
    private String movieGenre;
    private String movieCountry;
    @Column(columnDefinition = "LONGTEXT")
    private String movieDetail;
    private Integer movieRunningTime;
    private Long real_movieId;

    @Builder
    public Movie(Long real_movieId, String title, String movieImg, String movieBgImg,
        String movieActor,
        String movieDirector, String movieDate, String movieGenre, String movieCountry,
        String movieDetail, Integer movieRunningTime) {
        this.real_movieId = real_movieId;
        this.title = title;
        this.movieImg = movieImg;
        this.movieBgImg = movieBgImg;
        this.movieActor = movieActor;
        this.movieDirector = movieDirector;
        this.movieDate = movieDate;
        this.movieGenre = movieGenre;
        this.movieCountry = movieCountry;
        this.movieDetail = movieDetail;
        this.movieRunningTime = movieRunningTime;
    }


}
