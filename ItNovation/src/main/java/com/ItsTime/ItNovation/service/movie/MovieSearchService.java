package com.ItsTime.ItNovation.service.movie;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movie.dto.MovieResponseDto;
import com.ItsTime.ItNovation.domain.movie.dto.MovieSearchDto;
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

    public List<MovieResponseDto> searchMoviesByTitle(MovieSearchDto searchDto) {
        String search = searchDto.getSearch();
        List<Movie> movies = movieRepository.findByTitleContaining(searchDto.getSearch());
        List<MovieResponseDto> movieResponseDtos = new ArrayList<>();
        for (Movie movie : movies) {
            Float starScore = starRepository.findAvgScoreByMovieId(movie.getId());
            movieResponseDtos.add(convertToMovieResponse(movie, starScore));
        }
        return movieResponseDtos;
    }

    private MovieResponseDto convertToMovieResponse(Movie movie, Float starScore) {//starScore와 Movie Entity를 한곳에서 관리
        return new MovieResponseDto(
                movie.getId(),
                movie.getTitle(),
                movie.getMovieImg(),
                starScore
        );
    }
}


