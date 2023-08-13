package com.ItsTime.ItNovation.controller.movieTalk;


import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.movieTalk.TodayBestReviewService;
import com.ItsTime.ItNovation.service.movieTalk.TodayLatestReviewService;
import com.ItsTime.ItNovation.service.movieTalk.TodayPopularUserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/movie-talk/")
public class MovieTalkController {

    private final TodayPopularUserService todayPopularUserService;
    private final TodayLatestReviewService todayLatestReviewService;
    private final TodayBestReviewService todayBestReviewService;
    private final JwtService jwtService;

    @GetMapping("/best-review")
    public ResponseEntity todayBestReview(HttpServletRequest request){
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        return todayBestReviewService.getBestReviewAndUser(accessToken);
    }

    @GetMapping("/latest-review")
    public ResponseEntity LatestReviews(HttpServletRequest request){
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        return todayLatestReviewService.getLatestReviews(accessToken);
    }

    @GetMapping("/popular-user")
    public ResponseEntity getTopFollowers(HttpServletRequest request) {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        return todayPopularUserService.getTopFollowers(accessToken);
    }

}
