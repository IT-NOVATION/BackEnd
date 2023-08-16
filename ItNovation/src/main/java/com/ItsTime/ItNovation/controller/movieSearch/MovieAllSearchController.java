package com.ItsTime.ItNovation.controller.movieSearch;

import com.ItsTime.ItNovation.domain.movielog.dto.MovieLogResponseDto;
import com.ItsTime.ItNovation.service.movieSearch.MovieAllSearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/movie-search")
@Tag(name="무비서치 API")
public class MovieAllSearchController {
    private final MovieAllSearchService movieAllSearchService;

    @GetMapping("/review-order/{page}")
    @Operation(summary = "리뷰 수 기준")
    public ResponseEntity getMoiveSearchResponseByReviewOrder(@PathVariable int page) {

        return movieAllSearchService.getMoiveSearchResponseByReviewOrder(page);
    }
    @GetMapping("/star-score-order/{page}")
    @Operation(summary = "평점 수 기준")

    public ResponseEntity getMoiveSearchResponseByStarScoreOrder(@PathVariable int page) {

        return movieAllSearchService.getMoiveSearchResponseByStarScoreOrder(page);
    }

    @GetMapping("/release-order/{page}")
    @Operation(summary = "최근 개봉일 기준")

    public ResponseEntity getMoiveSearchResponseByLatestReleaseDate(@PathVariable int page) {

        return movieAllSearchService.getMoiveSearchResponseByLatestReleaseDate(page);
    }
}
