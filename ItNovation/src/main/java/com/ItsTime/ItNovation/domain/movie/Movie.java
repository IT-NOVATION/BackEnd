package com.ItsTime.ItNovation.domain.movie;


import com.ItsTime.ItNovation.domain.review.Review;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
    private String movieAudit;
    @Column(columnDefinition = "LONGTEXT")
    private String movieDetail;
    private Integer movieRunningTime;
    private Long real_movieId;


    @OneToMany(mappedBy = "reviewId")
    private List<Review> reviews = new ArrayList<Review>();

    @Builder
    public Movie(Long real_movieId, String title, String movieImg, String movieBgImg,
        String movieActor,
        String movieDirector, String movieDate, String movieGenre, String movieCountry,
        String movieDetail, Integer movieRunningTime, String movieAudit) {
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
        this.movieAudit = movieAudit;
    }

    public void updateMovie(Movie newInfoMovie){
        this.real_movieId = newInfoMovie.real_movieId;
        this.title = newInfoMovie.title;
        this.movieImg = newInfoMovie.movieImg;
        this.movieBgImg = newInfoMovie.movieBgImg;
        this.movieActor = newInfoMovie.movieActor;
        this.movieDirector = newInfoMovie.movieDirector;
        this.movieDate = newInfoMovie.movieDate;
        this.movieGenre = newInfoMovie.movieGenre;
        this.movieCountry = newInfoMovie.movieCountry;
        this.movieDetail = newInfoMovie.movieDetail;
        this.movieRunningTime = newInfoMovie.movieRunningTime;
        this.movieAudit = newInfoMovie.movieAudit;
    }


}
