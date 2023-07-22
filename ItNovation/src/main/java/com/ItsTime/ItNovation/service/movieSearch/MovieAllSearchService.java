package com.ItsTime.ItNovation.service.movieSearch;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.MovieRepository;
import com.ItsTime.ItNovation.domain.movieSearch.dto.MovieSearchDto;
import com.ItsTime.ItNovation.domain.movieSearch.dto.MovieSearchResponseDto;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.service.star.StarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class MovieAllSearchService {
    private final MovieRepository movieRepository;
    private final StarService starService;
    @Transactional
    public ResponseEntity getMoiveSearchResponseByReviewOrder(int page) {
        try{
            Pageable pageable = PageRequest.of(page - 1, 16);
            List<Movie> movieList = movieRepository.findMoviesWithReviewCount(pageable);
//            for (int i = 0; i <movieList.size() ; i++) {
//                if (movieList.get(i).getReviews().size() > 1) {
//                    log.info(String.valueOf(movieList.get(i).getReviews().get(1)));
//                }
//            }
            List<MovieSearchDto> movieSearchDtoList = new ArrayList<>();
            int lastPage = getLastPage();
            MovieSearchResponseDto movieSearchResponseDto = getMovieSearchResponseDto(page, movieList, movieSearchDtoList, lastPage);
            return ResponseEntity.status(HttpStatus.OK).body(movieSearchResponseDto);
        }catch (Exception e){
            return ResponseEntity.status(400).body("영화를 조회하는데 오류가 발생했습니다.");
        }

    }

    private MovieSearchResponseDto getMovieSearchResponseDto(int page, List<Movie> movieList, List<MovieSearchDto> movieSearchDtoList, int lastPage) {
        for (int i = 0; i < movieList.size(); i++) {
            Movie nowMovie = movieList.get(i);
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

}
