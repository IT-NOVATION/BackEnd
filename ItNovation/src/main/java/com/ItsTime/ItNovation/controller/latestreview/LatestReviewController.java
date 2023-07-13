package com.ItsTime.ItNovation.controller.latestreview;

import com.ItsTime.ItNovation.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LatestReviewController {
    private final ReviewService reviewService;

    @GetMapping("/test/today/latestReview")
    public ResponseEntity LatestReviews(){
        return reviewService.getLatestReviews();
    }

}
