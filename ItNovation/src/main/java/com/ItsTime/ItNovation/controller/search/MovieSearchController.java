package com.ItsTime.ItNovation.controller.search;

import com.ItsTime.ItNovation.domain.movie.dto.MovieSearchResponseDto;
import com.ItsTime.ItNovation.service.movie.MovieSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("search")
public class MovieSearchController {
    private final MovieSearchService movieSearchService;

    @GetMapping("/movie")
    public ResponseEntity <MovieSearchResponseDto> searchMoviesByTitle(@RequestParam(name="movieNm") String movieNM) {
        return movieSearchService.searchMoviesByTitle(movieNM);

    }
}


