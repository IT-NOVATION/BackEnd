package com.ItsTime.ItNovation.controller.bestreview;


import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.best.TodayBestReviewService;
import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("today")
public class TodayBestReviewController {


    private final TodayBestReviewService todayBestReviewService;
    private final JwtService jwtService;

    @GetMapping("/bestReview")
    public ResponseEntity todayBestReview(HttpServletRequest request){
        Optional<String> accessToken = jwtService.extractAccessToken(request);
        return todayBestReviewService.getBestReviewAndUser(accessToken);
    }





}
