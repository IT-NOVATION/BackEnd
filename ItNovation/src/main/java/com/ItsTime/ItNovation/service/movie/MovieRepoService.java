package com.ItsTime.ItNovation.service.movie;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import javax.swing.text.html.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class MovieRepoService {

    private final MovieRepository movieRepository;



    @Transactional
    public void saveMovie(Map<String, Movie> titleAndMovie){
        for (Entry<String, Movie> stringMovieEntry : titleAndMovie.entrySet()) {

            if(movieRepository.findByTitle(stringMovieEntry.getKey()).isEmpty()){
                movieRepository.save(stringMovieEntry.getValue());
            }
            log.info("DB에 이미 존재하고 있는 영화입니다.");
        }
    }


    @Transactional
    public Optional<Movie> findById(Long id){
        return movieRepository.findById(id);
    }


    @Transactional
    public Optional<Movie> findByTitle(String title){
        return movieRepository.findByTitle(title);
    }


    @Transactional
    public List<Movie> findAllMovies(){
        return movieRepository.findAll();
    }

}
