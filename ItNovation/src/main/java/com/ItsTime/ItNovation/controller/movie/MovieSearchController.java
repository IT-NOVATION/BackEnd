package com.ItsTime.ItNovation.controller.movie;

import com.ItsTime.ItNovation.domain.movie.dto.MovieResponseDto;
import com.ItsTime.ItNovation.domain.movie.dto.MovieSearchDto;
import com.ItsTime.ItNovation.service.movie.MovieSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MovieSearchController {
    private final MovieSearchService movieSearchService;

    @PostMapping("/search/movie")
    public List<MovieResponseDto> searchMoviesByTitle(@RequestBody MovieSearchDto searchDto) {
        return movieSearchService.searchMoviesByTitle(searchDto);
    }
}


