package com.ItsTime.ItNovation.controller.comment;


import com.ItsTime.ItNovation.domain.comment.dto.CommentWriteRequestDto;
import com.ItsTime.ItNovation.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
@Tag(name="댓글 관련 API")
@Slf4j
public class CommentController {


    private final CommentService commentService;
    @PostMapping("/write")
    @Operation(summary = "댓글 작성")

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
    @Operation(summary = "댓글 읽기")

    public ResponseEntity commentRead(@RequestParam("page") int page, @RequestParam("reviewId") Long reviewId){
        return commentService.readComment(page, reviewId);
    }



    @GetMapping("/delete/{commentId}")
    public ResponseEntity commentDelete(@PathVariable Long commentId){
        return commentService.deleteComment(commentId);
    }



}
