package com.ItsTime.ItNovation.controller.movie;

import com.ItsTime.ItNovation.domain.movie.dto.MoviePopularDto;
import com.ItsTime.ItNovation.domain.movie.dto.MovieSearchRequestDto;
import com.ItsTime.ItNovation.service.movie.MovieSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MovieSearchController {
    private final MovieSearchService movieSearchService;

    @PostMapping("/search/movie")
    public List<MoviePopularDto> searchMoviesByTitle(@RequestBody MovieSearchRequestDto searchDto) {
        System.out.println("searchDto.toString() = " + searchDto.toString());;
        return movieSearchService.searchMoviesByTitle(searchDto);
    }
}


