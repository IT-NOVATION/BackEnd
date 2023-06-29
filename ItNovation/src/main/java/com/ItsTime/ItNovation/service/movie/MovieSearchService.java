package com.ItsTime.ItNovation.service.movie;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movie.dto.MovieResponse;
import com.ItsTime.ItNovation.domain.movie.dto.SearchDto;
import com.ItsTime.ItNovation.domain.star.StarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MovieSearchService {
    private final MovieRepository movieRepository;
    private final StarRepository starRepository;

    @Autowired
    public MovieSearchService(MovieRepository movieRepository, StarRepository starRepository) {
        this.movieRepository = movieRepository;
        this.starRepository = starRepository;
    }

    public List<MovieResponse> searchMoviesByTitle(SearchDto searchDto) {
        String search = searchDto.getSearch();
        List<Movie> movies = movieRepository.findByTitleContaining(searchDto.getSearch());
        List<MovieResponse> movieResponses = new ArrayList<>();
        for (Movie movie : movies) {
            Float starScore = starRepository.findAverageScoreByMovieId(movie.getId());
            movieResponses.add(convertToMovieResponse(movie, starScore));
        }
        return movieResponses;
    }

    private MovieResponse convertToMovieResponse(Movie movie, Float starScore) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getMovieImg(),
                starScore
        );
    }
}


