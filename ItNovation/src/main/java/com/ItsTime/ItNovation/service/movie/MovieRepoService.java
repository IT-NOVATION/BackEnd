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
        for (Entry<String, Movie> newMovie : titleAndMovie.entrySet()) {
            try {
                Optional<Movie> byTitle = movieRepository.findByTitle(
                    newMovie.getValue().getTitle());

                if (byTitle.isPresent()) {
                    Movie movie = byTitle.get();
                    movie.updateMovie(newMovie.getValue());
                } else {
                    movieRepository.save(newMovie.getValue());
                }
            }catch (IllegalArgumentException e){
                log.info("이 영화에 대해서는 이미 값을 저장하고 있거나 해당 영화에 대해서는 오류가 발생하여 저장하지 않습니다.");
                continue;
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
