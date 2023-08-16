package com.ItsTime.ItNovation.controller.movie;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.dto.MoviePopularDto;
import com.ItsTime.ItNovation.domain.movie.dto.MoviePopularRecommendResponseDto;
import com.ItsTime.ItNovation.domain.movie.dto.MovieRecommendDto;
import com.ItsTime.ItNovation.service.movie.MovieCrawlService;
import com.ItsTime.ItNovation.service.movie.MovieRepoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movies")
@Tag(name="메인페이지 영화 관련 API")
@Slf4j
public class MovieController {

    private final MovieCrawlService movieCrawlService;
    private final MovieRepoService movieRepoService;

    // 캐시
    @GetMapping("/popular")

    public List<MoviePopularDto> getPopularMovies() {
        try{
            return movieCrawlService.getPopularMovies();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Operation(summary="메인페이지 인기영화 가져오기")
    @GetMapping("/popular-and-recommend")
    public ResponseEntity getPopularAndRecommend(){
        try {
            List<MoviePopularDto> popularMovies = getPopularTableMovies();
            List<MovieRecommendDto> topReviewedMovies = movieCrawlService.getTopReviewedMovies();
            if(topReviewedMovies.isEmpty()){
                throw new IllegalArgumentException("추천 영화 존재 x");
            }
            MoviePopularRecommendResponseDto moviePopularRecommendResponseDto = MoviePopularRecommendResponseDto.builder()
                .popular(popularMovies)
                .recommended(topReviewedMovies)
                .build();
            return ResponseEntity.status(200).body(moviePopularRecommendResponseDto);
        }catch (Exception e){
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    private List<MoviePopularDto> getPopularTableMovies() throws JsonProcessingException {
        List<MoviePopularDto> popularMoviesInTable = movieCrawlService.isPopularMoviesInTable();
        return popularMoviesInTable;
    }



}
