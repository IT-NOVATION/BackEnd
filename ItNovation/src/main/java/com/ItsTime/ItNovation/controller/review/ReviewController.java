package com.ItsTime.ItNovation.controller.review;

import com.ItsTime.ItNovation.domain.review.dto.ReviewPostRequestDto;
import com.ItsTime.ItNovation.domain.review.dto.ReviewReadRequestDto;

import com.ItsTime.ItNovation.domain.user.dto.LoginStateDto;
import com.ItsTime.ItNovation.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.review.ReviewService;
import com.auth0.jwt.exceptions.JWTDecodeException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
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
    private final JwtService jwtService;
    @PostMapping
    public ResponseEntity reviewWrite(@RequestBody ReviewPostRequestDto reviewPostRequestDto, Authentication authentication) {

            String nowUserEmail=authentication.getName();
            log.info("현재 로그인한 유저: {}", nowUserEmail);

            return reviewService.reviewWrite(reviewPostRequestDto, nowUserEmail);


    }

    @GetMapping("/Info/{reviewId}") // getMapping 으로 변경 -> id값 넘겨주기
    public ResponseEntity reviewRead(@PathVariable Long reviewId, HttpServletRequest request){
        Optional<String> s = jwtService.extractAccessToken(request);
        if(s.isPresent()){
            Optional<String> email = jwtService.extractEmail(s.get());
            if(email.isPresent()) {
                return reviewService.reviewRead(reviewId, email.get());
            }
        }
        return reviewService.reviewRead(reviewId, null);
    }




    @GetMapping("/movieInfo/{movieId}")
    public ResponseEntity reviewWriteGetMovieInfo(@PathVariable Long movieId, Authentication authentication)
    {
        return reviewService.getMovieInfo(movieId,authentication);
    }




}
