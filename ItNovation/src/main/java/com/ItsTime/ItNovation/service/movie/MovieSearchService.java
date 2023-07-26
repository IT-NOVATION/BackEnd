package com.ItsTime.ItNovation.service.movie;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movie.dto.MoviePopularDto;
import com.ItsTime.ItNovation.domain.movie.dto.MovieSearchResponseDto;
import com.ItsTime.ItNovation.domain.star.StarRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieSearchService {
    private final MovieRepository movieRepository;
    private final StarRepository starRepository;

    @Transactional
    public ResponseEntity <MovieSearchResponseDto> searchMoviesByTitle(String movieNM) {
        List<Movie> movies = movieRepository.findByTitleContaining(movieNM);
        List<MoviePopularDto> moviePopularDtos = new ArrayList<>();
        for (Movie movie : movies) {
            Float starScore = starRepository.findAvgScoreByMovieId(movie.getId());
            if(starScore == null){
                starScore = 0.0f;
            }
            moviePopularDtos.add(convertToMovieResponse(movie, starScore));
        }
        int size = moviePopularDtos.size();//영화 배열 크기
        MovieSearchResponseDto movieSearchResponseDto = MovieSearchResponseDto.builder()
                .movieSearchResult(moviePopularDtos)
                .totalSize(size)
                .build();
        return ResponseEntity.status(200).body(movieSearchResponseDto);
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


