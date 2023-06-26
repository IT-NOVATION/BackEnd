package com.ItsTime.ItNovation.service.movie;

import com.ItsTime.ItNovation.domain.movie.Movie;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class MovieRepoServiceTest {

    @Autowired
    MovieRepoService movieRepoService;

    @Test
    void save_Find_Test() {
//        Movie movie = new Movie("test1", "testUrl");
//        movieRepoService.save(movie);

//        Optional<Movie> movieOptional = movieRepoService.findById(movie.getMovieId());
//
//        Movie findMovie= movieOptional.get();
//        Assertions.assertThat(movie.getMovieId()).isEqualTo(findMovie.getMovieId());
    }


//     this.real_movieId = real_movieId;
//        this.title = title;
//        this.movieImg = movieImg;
//        this.movieBgImg = movieBgImg;
//        this.movieActor = movieActor;
//        this.movieDirector = movieDirector;
//        this.movieDate = movieDate;
//        this.movieGenre = movieGenre;
//        this.movieCountry = movieCountry;
//        this.movieDetail = movieDetail;
//        this.movieRunningTime = movieRunningTime;
    @Test
    void find_title_test(){
        Movie movie = Movie.builder().
            movieCountry("en").movieGenre("12").movieRunningTime(123).movieDirector("123")
            .movieDetail("fwefsv").movieDate("1234").movieActor("12345")
            .title("helloworld").movieImg("1234").movieBgImg("12345").real_movieId(444332L).build();

        Map<String, Movie> movieInfo = new HashMap<>();
        movieInfo.put("helloworld", movie);
        movieRepoService.saveMovie(movieInfo);

        Optional<Movie> findByTitleMovie = movieRepoService.findByTitle("helloworld");
        Movie movie1 = findByTitleMovie.get();

        Assertions.assertThat(movie.getTitle()).isEqualTo(movie1.getTitle());

    }



}