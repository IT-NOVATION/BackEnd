package com.ItsTime.ItNovation.controller.movieTalk;


import com.ItsTime.ItNovation.config.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.movieTalk.TodayBestReviewService;
import com.ItsTime.ItNovation.service.movieTalk.TodayLatestReviewService;
import com.ItsTime.ItNovation.service.movieTalk.TodayPopularUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="무비토크 API")
public class MovieTalkController {

    private final TodayPopularUserService todayPopularUserService;
    private final TodayLatestReviewService todayLatestReviewService;
    private final TodayBestReviewService todayBestReviewService;
    private final JwtService jwtService;

    @GetMapping("/best-review")
    @Operation(summary="베스트 리뷰")
    public ResponseEntity todayBestReview(HttpServletRequest request){
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        return todayBestReviewService.getBestReviewAndUser(accessToken);
    }

    @GetMapping("/latest-review")
    @Operation(summary="최신 리뷰")

    public ResponseEntity LatestReviews(HttpServletRequest request){
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        return todayLatestReviewService.getLatestReviews(accessToken);
    }

    @GetMapping("/popular-user")
    @Operation(summary="인기 유저")
    public ResponseEntity getTopFollowers(HttpServletRequest request) {
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        return todayPopularUserService.getTopFollowers(accessToken);
    }

}
