package com.ItsTime.ItNovation.controller.latestreview;

import com.ItsTime.ItNovation.domain.review.dto.LatestReviewResponseDto;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.review.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LatestReviewController {
    private final ReviewService reviewService;
    private final JwtService jwtService;

    @GetMapping("/today/latestReview")
    public ResponseEntity LatestReviews(HttpServletRequest request){
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        return reviewService.getLatestReviews(accessToken);


    }

}
