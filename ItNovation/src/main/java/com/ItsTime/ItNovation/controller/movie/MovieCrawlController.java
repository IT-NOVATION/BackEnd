package com.ItsTime.ItNovation.controller.movie;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.service.movie.MovieCrawlService;
import com.ItsTime.ItNovation.service.movie.MovieRepoService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crawl")
@Slf4j
public class MovieCrawlController {


    private final MovieCrawlService movieCrawlService;
    private final MovieRepoService movieRepoService;

    @GetMapping // 테스트 하실때는 SecurityConfig에 /movies url 추가후 진행하세요!
    public Map<String, Movie> getMovies() {
        Map<String, Movie> titleAndMovie = movieCrawlService.getTitleAndMovie(); // 이 부분 무조건 고쳐야 함. 동기적으로 데이터 가져와서 스케줄링 같은 작업으로 일정 주기에 끌어오는 방법 고안.
        movieRepoService.saveMovie(titleAndMovie);
        return titleAndMovie;
    }


}
