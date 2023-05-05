package com.ItsTime.ItNovation.service.movie;

import com.ItsTime.ItNovation.entity.movie.Movie;
import com.ItsTime.ItNovation.entity.movie.MovieRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class MovieRepoService {

    private final MovieRepository movieRepository;


    @Transactional
    public Movie save(Movie movie){
        return movieRepository.save(movie);
    }


    @Transactional
    public Optional<Movie> findById(Long id){
        return movieRepository.findById(id);
    }


}
