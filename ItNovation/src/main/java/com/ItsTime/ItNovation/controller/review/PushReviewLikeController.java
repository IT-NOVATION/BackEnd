package com.ItsTime.ItNovation.controller.review;


import com.ItsTime.ItNovation.service.push.PushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PushReviewLikeController {

    private final PushService pushService;


    @GetMapping("/test/push/reviewlike")
    public ResponseEntity pushReviewLike(@RequestParam(name="reviewId") Long reviewId, @RequestParam(name="userId") Long userId){
        log.info(reviewId.toString());
        log.info(userId.toString());

        return pushService.pushReviewLike(reviewId, userId);
    }











}
