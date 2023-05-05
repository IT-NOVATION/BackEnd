package com.ItsTime.ItNovation.service.movie;

import static org.junit.jupiter.api.Assertions.*;

import com.ItsTime.ItNovation.entity.movie.Movie;
import com.ItsTime.ItNovation.entity.movie.MovieRepository;
import jakarta.transaction.Transactional;
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
        Movie movie = new Movie("test1", "testUrl");
        movieRepoService.save(movie);

        Optional<Movie> movieOptional = movieRepoService.findById(movie.getMovieId());

        Movie findMovie= movieOptional.get();
        Assertions.assertThat(movie.getMovieId()).isEqualTo(findMovie.getMovieId());
    }



}