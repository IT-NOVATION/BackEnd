package com.ItsTime.ItNovation.controller.comment;


import com.ItsTime.ItNovation.domain.comment.dto.CommentWriteRequestDto;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import com.ItsTime.ItNovation.notify.NotificationService;
import com.ItsTime.ItNovation.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final NotificationService notificationService;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    private final CommentService commentService;
    @PostMapping("/write")
    public ResponseEntity commentWrite(@RequestBody CommentWriteRequestDto commentDto, Authentication authentication){
        //리뷰댓글다는 사람(pub)
        String email = authentication.getName();
        Optional<User> pubuser = userRepository.findByEmail(email);
        if(pubuser.isPresent()){
            Long pubId=pubuser.get().getId();
            //리뷰주인- 알람받는사람(sub)
            Long reviewId = commentDto.getReviewId();
            Long subId=reviewRepository.findById(reviewId).get().getUser().getId();
            //subId에게 리뷰댓글다는 사람의 id 를 알려주고 이벤트 카테고리는 리뷰댓글
            //프론트엔드가 이제 subId로 sse 연결을 맺고 pubId님이 댓글을 달았다고 공지 주면됨
            notificationService.notify(subId,pubId,"comment");
        }

        return commentService.writeComment(commentDto, email);
    }


    /**
     *
     * @param page 당 댓글 5개
     * @return
     */
    @GetMapping("/read")
    public ResponseEntity commentRead(@RequestParam("page") int page, @RequestParam("reviewId") Long reviewId){
        return commentService.readComment(page, reviewId);
    }



    @GetMapping("/delete/{commentId}")
    public ResponseEntity commentDelete(@PathVariable Long commentId){
        return commentService.deleteComment(commentId);
    }



}
