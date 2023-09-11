package com.ItsTime.ItNovation.service.movieSearch;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movieSearch.dto.MovieSearchDto;
import com.ItsTime.ItNovation.domain.movieSearch.dto.MovieSearchResponseDto;
import com.ItsTime.ItNovation.domain.star.StarRepository;
import com.ItsTime.ItNovation.service.star.StarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class MovieAllSearchService {
    private final MovieRepository movieRepository;
    private final StarService starService;
    private final StarRepository starRepository;
    @Transactional
    public ResponseEntity getMoiveSearchResponseByReviewOrder(int page) {
        try{

            List<Movie> movieList = movieRepository.findMoviesWithSortedReviewCount();

            List<MovieSearchDto> movieSearchDtoList = new ArrayList<>();
            int pageSize = 16;
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, movieList.size());
            List<Movie> moviesForPage = movieList.subList(startIndex, endIndex);
            int lastPage = (int) Math.ceil((double) movieList.size() / pageSize);
            MovieSearchResponseDto movieSearchResponseDto = getMovieSearchResponseDto(page, moviesForPage, movieSearchDtoList, lastPage);


            return ResponseEntity.status(HttpStatus.OK).body(movieSearchResponseDto);
        }catch (Exception e){
            return ResponseEntity.status(400).body("영화를 조회하는데 오류가 발생했습니다.");
        }

    }

    private MovieSearchResponseDto getMovieSearchResponseDto(int page, List<Movie> movieList, List<MovieSearchDto> movieSearchDtoList, int lastPage) {
        for (int i = 0; i < movieList.size(); i++) {
            Movie nowMovie = movieList.get(i);
            log.info(nowMovie.getMovieDate());
            log.info(String.valueOf(nowMovie.getReviews().size()));
            MovieSearchDto movieSearchDto = BuildMovieSearchDto(nowMovie);

            movieSearchDtoList.add(movieSearchDto);
        }
        return MovieSearchResponseDto
                .builder().moiveSearchDtoList(movieSearchDtoList)
                .lastPage(lastPage)
                .nowPage(page)
                .firstPage(1)
                .build();
    }

    private MovieSearchDto BuildMovieSearchDto(Movie nowMovie) {

        return MovieSearchDto.builder()
                .movieId(nowMovie.getId())
                .movieTitle(nowMovie.getTitle())
                .movieImg(nowMovie.getMovieImg())
                .starScore(starService.getMovieAvgScore(nowMovie.getId()))
                .reviewCount(nowMovie.getReviews().size()).build();
    }

    private int getLastPage() {
        if(movieRepository.findAll().size()%16==0){
            return movieRepository.findAll().size()/16;
        }
        return movieRepository.findAll().size()/16+1;
    }

    @Transactional
    public ResponseEntity getMoiveSearchResponseByStarScoreOrder(int page) {
        try{
            List<Movie> movieList = movieRepository.findAll();
            List<MovieSearchDto> movieSearchDtoList = new ArrayList<>();
            for (Movie m : movieList) {
                Optional<Float> avgscore = Optional.of(starRepository.findAvgScoreByMovieIdOnMovieLog(m.getId()).orElse(0f));
                MovieSearchDto movieSearchDto = MovieSearchDto.builder()
                        .movieId(m.getId())
                        .starScore(avgscore.get())
                        .movieTitle(m.getTitle())
                        .movieImg(m.getMovieImg())
                        .reviewCount(m.getReviews().size())
                        .build();

                movieSearchDtoList.add(movieSearchDto);
            }
            movieSearchDtoList.sort(Comparator.comparingDouble((MovieSearchDto m) -> m.getStarScore().doubleValue()).reversed());

            int pageSize = 16;
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, movieList.size());

            List<MovieSearchDto> moviesForPage = movieSearchDtoList.subList(startIndex, endIndex);
            int lastPage = (int) Math.ceil((double) movieList.size() / pageSize);
            MovieSearchResponseDto movieSearchResponseDto = getMovieSearchResponseDto(page, moviesForPage, lastPage);
            return ResponseEntity.status(HttpStatus.OK).body(movieSearchResponseDto);
        }catch (Exception e){
            return ResponseEntity.status(400).body("영화를 조회하는데 오류가 발생했습니다.");
        }

    }

    private MovieSearchResponseDto getMovieSearchResponseDto(int page, List<MovieSearchDto> moviesForPage, int lastPage) {
        return MovieSearchResponseDto
                .builder().moiveSearchDtoList(moviesForPage)
                .lastPage(lastPage)
                .nowPage(page)
                .firstPage(1)
                .build();
    }

    @Transactional
    public ResponseEntity getMoiveSearchResponseByLatestReleaseDate(int page) {
        try{
            Pageable pageable = PageRequest.of(page - 1, 16);
            List<Movie> movieList = movieRepository.moviesByReleaseDate(pageable);

            List<MovieSearchDto> movieSearchDtoList = new ArrayList<>();
            int lastPage = getLastPage();
            MovieSearchResponseDto movieSearchResponseDto = getMovieSearchResponseDto(page, movieList, movieSearchDtoList, lastPage);
            return ResponseEntity.status(HttpStatus.OK).body(movieSearchResponseDto);
        }catch (Exception e){
            return ResponseEntity.status(400).body("영화를 조회하는데 오류가 발생했습니다.");
        }
    }
}
