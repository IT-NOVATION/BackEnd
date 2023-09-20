package com.ItsTime.ItNovation.controller.movie;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.service.movie.MovieCrawlService;
import com.ItsTime.ItNovation.service.movie.MovieRepoService;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crawl")
@Tag(name="영화 크롤링 API")
@Slf4j
public class MovieCrawlController {


    private final MovieCrawlService movieCrawlService;
    private final MovieRepoService movieRepoService;

    @GetMapping
    @Operation(summary = "영화 크롤링")
    public Map<String, Movie> getMovies() {
        Map<String, Movie> titleAndMovie = movieCrawlService.getTitleAndMovie();
        movieRepoService.saveMovie(titleAndMovie);
        return titleAndMovie;
    }


}
