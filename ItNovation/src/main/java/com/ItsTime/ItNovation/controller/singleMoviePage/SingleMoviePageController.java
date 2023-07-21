package com.ItsTime.ItNovation.controller.singleMoviePage;


import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.singleMoviePage.SingleMoviePageService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
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
    private final JwtService jwtService;

    @GetMapping("/moviePage/{movieId}")
    public ResponseEntity moviePage(@PathVariable Long movieId, HttpServletRequest request){
        Optional<String> s = jwtService.extractAccessToken(request);
        if(s.isPresent()){
            Optional<String> email = jwtService.extractEmail(s.get());
            if(email.isPresent()) {
                return singleMoviePageService.getReviewInformationAboutMovie(movieId, email.get());
            }
        }
        log.info("로그인 상태가 아닙니다.");
        return singleMoviePageService.getReviewInformationAboutMovie(movieId, null);
    }




}
