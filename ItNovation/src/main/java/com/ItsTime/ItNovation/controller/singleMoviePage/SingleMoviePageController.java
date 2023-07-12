package com.ItsTime.ItNovation.controller.singleMoviePage;


import com.ItsTime.ItNovation.service.singleMoviePage.SingleMoviePageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/single")
@Slf4j
public class SingleMoviePageController {

    private final SingleMoviePageService singleMoviePageService;

    @GetMapping("/moviePage/{movieId}")
    public ResponseEntity moviePage(@PathVariable Long movieId){
        return singleMoviePageService.getReviewInformationAboutMovie(movieId);
    }


}
