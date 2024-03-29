package com.ItsTime.ItNovation.controller.push;


import com.ItsTime.ItNovation.common.exception.ErrorCode;
import com.ItsTime.ItNovation.common.exception.NotFoundException;
import com.ItsTime.ItNovation.domain.follow.dto.TargetUserRequestDto;
import com.ItsTime.ItNovation.domain.movieLike.dto.MovieLikeRequestDto;

import com.ItsTime.ItNovation.domain.reviewLike.dto.ReviewLikeRequestDto;

import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.service.push.PushService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
// 코드 응집성을 높이기 위해 push하에 통합한 case
@RequestMapping("/api/v1/push")
@Slf4j
@Tag(name="좋아요 기능, 팔로잉 기능 API")
public class PushController {

    private final PushService pushService;
    private final UserRepository userRepository;



    @Operation(summary = "리뷰 좋아요")
    @PostMapping("/review-like")
    public ResponseEntity pushReviewLike(@RequestBody  ReviewLikeRequestDto reviewLikeRequestDto, Authentication authentication){
        String email = authentication.getName();
        try {
            User findUser = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
            log.info(String.valueOf(findUser.getId()));
            return pushService.pushReviewLike(reviewLikeRequestDto, findUser);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
    }


    /**
     * 1. follow 눌렀을때 상대방과 follow가 안돼있는 case
     * 2. follow 테이블이 존재하는데 해제하고 싶은 case
     * @return response
     */

    @Operation(summary = "팔로잉 하기")
    @PostMapping("/follow")
    public ResponseEntity pushFollow(@RequestBody TargetUserRequestDto targetUserRequestDto,Authentication authentication){
            try {
                User user = userRepository.findByEmail(authentication.getName())
                        .orElseThrow(() -> new IllegalArgumentException("no user"));
                return pushService.pushFollow(user.getId(), targetUserRequestDto.getTargetUserId());
            }catch (IllegalArgumentException e){
                return ResponseEntity.status(400).body(e.getMessage());
            }
    }

    @PostMapping("/movie-like")
    @Operation(summary = "영화 좋아요")
    public ResponseEntity pushMovieLike(@RequestBody MovieLikeRequestDto movieLikeRequestDto, Authentication authentication) {
        try {
            User user = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("no user"));
            return pushService.pushMovieLike(user.getId(), movieLikeRequestDto.getMovieId());

        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
