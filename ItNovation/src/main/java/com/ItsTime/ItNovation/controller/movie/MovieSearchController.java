package com.ItsTime.ItNovation.controller.movie;

import com.ItsTime.ItNovation.domain.movie.dto.MovieResponse;
import com.ItsTime.ItNovation.domain.movie.dto.SearchDto;
import com.ItsTime.ItNovation.service.movie.MovieSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieSearchController {
    private final MovieSearchService movieSearchService;

    @Autowired
    public MovieSearchController(MovieSearchService movieSearchService) {
        this.movieSearchService = movieSearchService;
    }

    @PostMapping("/search/movie")
    public List<MovieResponse> searchMoviesByTitle(@RequestBody SearchDto searchDto) {
        return movieSearchService.searchMoviesByTitle(searchDto);
    }
}


