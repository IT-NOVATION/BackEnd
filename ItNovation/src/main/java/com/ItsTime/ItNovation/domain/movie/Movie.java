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


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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


    @OneToMany(mappedBy = "movie")
    private List<Review> reviews = new ArrayList<Review>();

    @Builder
    public Movie(Long real_movieId, String title, String movieImg, String movieBgImg,
        String movieDirector, String movieDate, String movieGenre, String movieCountry,
        String movieDetail, Integer movieRunningTime, String movieAudit) {
        this.real_movieId = real_movieId;
        this.title = title;
        this.movieImg = movieImg;
        this.movieBgImg = movieBgImg;
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
        this.movieDirector = newInfoMovie.movieDirector;
        this.movieDate = newInfoMovie.movieDate;
        this.movieGenre = newInfoMovie.movieGenre;
        this.movieCountry = newInfoMovie.movieCountry;
        this.movieDetail = newInfoMovie.movieDetail;
        this.movieRunningTime = newInfoMovie.movieRunningTime;
        this.movieAudit = newInfoMovie.movieAudit;
    }


}
