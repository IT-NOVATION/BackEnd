package com.ItsTime.ItNovation.controller.bestreview;


import com.ItsTime.ItNovation.service.best.TodayBestReviewService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TodayBestReviewController {


    private final TodayBestReviewService todayBestReviewService;

    @GetMapping("/today/bestReview")
    public ResponseEntity todayBestReview(){

        return todayBestReviewService.getBestReviewAndUser();
    }





}
