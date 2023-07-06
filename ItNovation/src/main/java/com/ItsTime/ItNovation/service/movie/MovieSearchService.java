package com.ItsTime.ItNovation.service.movie;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movie.dto.MoviePopularDto;
import com.ItsTime.ItNovation.domain.movie.dto.MovieSearchRequestDto;
import com.ItsTime.ItNovation.domain.star.StarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieSearchService {
    private final MovieRepository movieRepository;
    private final StarRepository starRepository;

    public List<MoviePopularDto> searchMoviesByTitle(MovieSearchRequestDto searchDto) {
        String search = searchDto.getSearch();
        List<Movie> movies = movieRepository.findByTitleContaining(searchDto.getSearch());
        List<MoviePopularDto> moviePopularDtos = new ArrayList<>();
        for (Movie movie : movies) {
            Float starScore = starRepository.findAvgScoreByMovieId(movie.getId());
            if(starScore == null){
                starScore = 0.0f;
            }
            moviePopularDtos.add(convertToMovieResponse(movie, starScore));
        }
        return moviePopularDtos;
    }

    private MoviePopularDto convertToMovieResponse(Movie movie, Float starScore) {//starScore와 Movie Entity를 한곳에서 관리
        return MoviePopularDto.builder()
                .movieId(movie.getId())
                .movieTitle(movie.getTitle())
                .movieImg(movie.getMovieImg())
                .starScore(starScore)
                .build();
    }
}


