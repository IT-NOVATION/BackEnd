package com.ItsTime.ItNovation.controller.push;


import com.ItsTime.ItNovation.service.push.PushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/push")
@Slf4j
public class PushController {

    private final PushService pushService;


    @GetMapping("/reviewlike")
    public ResponseEntity pushReviewLike(@RequestParam(name="reviewId") Long reviewId, @RequestParam(name="userId") Long userId){
        log.info(reviewId.toString());
        log.info(userId.toString());

        return pushService.pushReviewLike(reviewId, userId);
    }


    /**
     * 1. follow 눌렀을때 상대방과 follow가 안돼있는 case
     * 2. follow 테이블이 존재하는데 해제하고 싶은 case
     *
     * @param targetId
     * @param pushUserId
     * @return response
     */
    @GetMapping("/follow")
    public ResponseEntity pushFollow(@RequestParam(name="pushUserId") Long pushUserId, @RequestParam(name="targetUserId") Long targetId){
        return pushService.pushFollow(pushUserId, targetId);
    }

}
