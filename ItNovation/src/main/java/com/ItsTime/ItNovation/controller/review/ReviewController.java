package com.ItsTime.ItNovation.controller.review;

import com.ItsTime.ItNovation.domain.review.dto.ReviewPostRequestDto;
import com.ItsTime.ItNovation.domain.review.dto.ReviewReadRequestDto;
import com.ItsTime.ItNovation.domain.review.dto.ReviewReadResponseDto;
import com.ItsTime.ItNovation.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("review")
public class ReviewController {

    private final ReviewService reviewService;
    @PostMapping
    public ResponseEntity reviewWrite(@RequestBody ReviewPostRequestDto reviewPostRequestDto, Authentication authentication) {

            String nowUserEmail=authentication.getName();
            log.info("현재 로그인한 유저: {}", nowUserEmail);

            return reviewService.reviewWrite(reviewPostRequestDto, nowUserEmail);


    }

    @PostMapping("/Info")
    public ResponseEntity reviewRead(@RequestBody ReviewReadRequestDto reviewReadRequestDto){

        Long reviewId = reviewReadRequestDto.getReviewId();
        return reviewService.reviewRead(reviewId);
    }




}
