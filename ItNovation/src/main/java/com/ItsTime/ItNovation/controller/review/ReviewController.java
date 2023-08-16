package com.ItsTime.ItNovation.controller.review;

import com.ItsTime.ItNovation.common.exception.UnauthorizedException;
import com.ItsTime.ItNovation.domain.review.dto.ReviewPostRequestDto;

import com.ItsTime.ItNovation.config.jwt.service.JwtService;
import com.ItsTime.ItNovation.service.review.ReviewService;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="리뷰 API")
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtService jwtService;
    @PostMapping("/write")
    public ResponseEntity reviewWrite(@RequestBody ReviewPostRequestDto reviewPostRequestDto, Authentication authentication) {

            String nowUserEmail=authentication.getName();
            log.info("현재 로그인한 유저: {}", nowUserEmail);

            return reviewService.reviewWrite(reviewPostRequestDto, nowUserEmail);


    }

    @Operation(summary="리뷰 읽기 페이지에서 리뷰 정보, 영화 정보 가져오기")
    @GetMapping("/info/{reviewId}")
    public ResponseEntity reviewRead(@PathVariable Long reviewId, HttpServletRequest request){
        Optional<String> s = jwtService.extractAccessToken(request);
        if(s.isPresent()){
            Optional<String> email = jwtService.extractEmail(s.get());
            try{
                if(email.isPresent()) {
                    return reviewService.reviewRead(reviewId, email.get());
                }
            }catch(UnauthorizedException e){

            }

        }
        return reviewService.reviewRead(reviewId, null);
    }



    @Operation(summary="리뷰 작성 페이지에서 영화 정보 가져오기")
    @GetMapping("/movie-info/{movieId}")
    public ResponseEntity reviewWriteGetMovieInfo(@PathVariable Long movieId, Authentication authentication)
    {
        return reviewService.getMovieInfo(movieId,authentication);
    }

    @Operation(summary="리뷰 읽기 페이지에서 좋아요를 누른 사람 목록")
    @GetMapping("/info/like-user/{reviewId}")
    public ResponseEntity reviewInfoLikeUser(@PathVariable Long reviewId, HttpServletRequest request){

        Optional<String> s = jwtService.extractAccessToken(request);
        if(s.isPresent()){
            Optional<String> email = jwtService.extractEmail(s.get());
            try{
                if(email.isPresent()) {
                    return reviewService.getLikeUsers(reviewId, email.get());
                }
            }catch(UnauthorizedException e){

            }
        }
        return reviewService.getLikeUsers(reviewId, null);
    }



}
