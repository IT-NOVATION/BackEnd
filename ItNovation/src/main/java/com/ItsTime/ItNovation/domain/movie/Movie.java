package com.ItsTime.ItNovation.domain.movie;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

@Entity
@Data
@NoArgsConstructor
public class Movie {
//[adult, backdrop_path, id, original_language, overview, popularity, release_date, title,  vote_average, vote_count]


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean adult;
    private String backdrop_path;

    private Integer movie_unique_id;

    private String original_language;

    @Column(columnDefinition="LONGTEXT")
    private String overview;

    private String release_date;
    private String movieTitle;

    private String moviePosterUrl;

    @Builder // 끌고 올 수 있는 최대한의 의미있는 데이터들
    public Movie(Boolean adult, String backdrop_path, Integer movie_unique_id, String original_language,
        String overview, String release_date,
        String movieTitle, String moviePosterUrl) {
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.movie_unique_id = movie_unique_id;
        this.original_language = original_language;
        this.overview = overview;
        this.release_date = release_date;
        this.movieTitle = movieTitle;
        this.moviePosterUrl = moviePosterUrl;
    }

    @Builder // 테스트를 위하여 남겨놓은 곳
    public Movie(String title, String url) {
       this.movieTitle=title;
       this.moviePosterUrl=url;
    }




}
