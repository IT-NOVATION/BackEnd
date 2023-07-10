package com.ItsTime.ItNovation.controller.single;

import com.ItsTime.ItNovation.domain.star.StarRepository;
import com.ItsTime.ItNovation.domain.star.dto.SingleStarEvaluateRequestDto;
import com.ItsTime.ItNovation.service.review.ReviewService;
import com.ItsTime.ItNovation.service.star.StarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test/single")
public class SingleController {

    private final StarService starService;
    private final ReviewService reviewService;

    @PostMapping("/starEvaluate")
    public ResponseEntity singleStarEvaluate(@RequestBody SingleStarEvaluateRequestDto singleStarEvaluateRequestDto){
        Long userId = singleStarEvaluateRequestDto.getUserId();

        log.info(String.valueOf(userId));
        return starService.singleStarEvaluate(singleStarEvaluateRequestDto);
    }
    @GetMapping("/movie/reviewCount/{movieId}")
    public ResponseEntity reviewCount(@PathVariable Long movieId){
        return reviewService.reviewCount(movieId);
    }
}
