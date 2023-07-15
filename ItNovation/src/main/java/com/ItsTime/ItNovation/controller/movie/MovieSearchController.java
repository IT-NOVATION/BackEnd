package com.ItsTime.ItNovation.controller.movie;

import com.ItsTime.ItNovation.domain.movie.dto.MoviePopularDto;
import com.ItsTime.ItNovation.domain.movie.dto.MovieSearchRequestDto;
import com.ItsTime.ItNovation.service.movie.MovieSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("search")
public class MovieSearchController {
    private final MovieSearchService movieSearchService;

    @GetMapping("/movie")
    public List<MoviePopularDto> searchMoviesByTitle(@RequestParam(name="movieNm") String movieNM) {
        return movieSearchService.searchMoviesByTitle(movieNM);
    }
}


