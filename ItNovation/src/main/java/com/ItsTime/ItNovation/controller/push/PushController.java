package com.ItsTime.ItNovation.controller.push;


import com.ItsTime.ItNovation.domain.movieLike.dto.MovieLikeRequestDto;
import com.ItsTime.ItNovation.service.push.PushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/push")
@Slf4j
public class PushController {

    private final PushService pushService;


    @GetMapping("/reviewlike") //userId 빼기
    public ResponseEntity pushReviewLike(@RequestParam(name="reviewId") Long reviewId, @RequestParam(name="userId") Long userId){
        log.info(reviewId.toString());
        log.info(userId.toString());

        return pushService.pushReviewLike(reviewId, userId);
    }


    /**
     * 1. follow 눌렀을때 상대방과 follow가 안돼있는 case
     * 2. follow 테이블이 존재하는데 해제하고 싶은 case
     * @return response
     */
    @GetMapping("/follow") //userId 빼기
    public ResponseEntity pushFollow(@RequestParam(name="pushUserId") Long pushUserId, @RequestParam(name="targetUserId") Long targetId){
        return pushService.pushFollow(pushUserId, targetId);
    }


    @GetMapping("/movieLike") // usdrId 빼기
    public ResponseEntity pushMovieLike(@RequestBody MovieLikeRequestDto movieLikeRequestDto){
        Long userId = movieLikeRequestDto.getUserId();
        Long movieId = movieLikeRequestDto.getMovieId();
        return pushService.pushMovieLike(userId, movieId);
    }
}
