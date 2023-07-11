package com.ItsTime.ItNovation.controller.push;


import com.ItsTime.ItNovation.domain.movieLike.dto.MovieLikeRequestDto;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.service.push.PushService;
import com.ItsTime.ItNovation.service.user.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/push")
@Slf4j
public class PushController {

    private final PushService pushService;
    private final UserRepository userRepository;


    @GetMapping("/reviewlike") //userId 빼기
    public ResponseEntity pushReviewLike(@RequestParam(name="reviewId") Long reviewId, Authentication authentication){
        try {
            User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("hi"));
            return pushService.pushReviewLike(reviewId, user.getId());
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(null);
        }
    }


    /**
     * 1. follow 눌렀을때 상대방과 follow가 안돼있는 case
     * 2. follow 테이블이 존재하는데 해제하고 싶은 case
     * @return response
     */
    @GetMapping("/follow") //userId 빼기
    public ResponseEntity pushFollow(@RequestParam(name="targetUserId") Long targetId, Authentication authentication){
        try {
            User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("hi"));
            return pushService.pushFollow(user.getId(), targetId);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(null);
        }
    }


    @GetMapping("/movieLike") // usdrId 빼기
    public ResponseEntity pushMovieLike(@RequestParam Long movieId, Authentication authentication) {
        try {
            User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("hi"));
            return pushService.pushMovieLike(user.getId(), movieId);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(null);
        }
    }
}
