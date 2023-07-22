package com.ItsTime.ItNovation.controller.comment;


import com.ItsTime.ItNovation.domain.comment.dto.CommentWriteRequestDto;
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

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {


    private final CommentService commentService;
    @PostMapping("/write")
    public ResponseEntity commentWrite(@RequestBody CommentWriteRequestDto commentDto, Authentication authentication){
        String email = authentication.getName();

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
