package com.ItsTime.ItNovation.controller.movieSearch;

import com.ItsTime.ItNovation.domain.movielog.dto.MovieLogResponseDto;
import com.ItsTime.ItNovation.service.movieSearch.MovieAllSearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/movie-search")
public class MovieAllSearchController {
    private final MovieAllSearchService movieAllSearchService;

    @GetMapping("/review-order/{page}")
    public ResponseEntity getMoiveSearchResponseByReviewOrder(@PathVariable int page) {

        return movieAllSearchService.getMoiveSearchResponseByReviewOrder(page);
    }
    @GetMapping("/star-score-order/{page}")
    public ResponseEntity getMoiveSearchResponseByStarScoreOrder(@PathVariable int page) {

        return movieAllSearchService.getMoiveSearchResponseByStarScoreOrder(page);
    }
}
