package com.ItsTime.ItNovation.controller.review;

import com.ItsTime.ItNovation.domain.review.dto.ReviewDTO;
import com.ItsTime.ItNovation.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    /*
    @Autowired//현재 사용 불가능 reviewService 제대로 완성 X
    private final ReviewService reviewService;

    @GetMapping("/movieId/all")//영화의 모든 리뷰
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("movieId") Long movieId){
        List<ReviewDTO> reviewDTOList = reviewService.getListOfMovie(movieId);

        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

    @PutMapping("/{movieId}/reviewId")//리뷰 수정, 성공 여부
    public ResponseEntity<Long> modifyReview(@PathVariable Long reviewId, @RequestBody ReviewDTO movieReivewDTO){
        reviewService.modify(movieReivewDTO);
        return new ResponseEntity<>(reviewId, HttpStatus.OK);
    }

    @DeleteMapping("/{movieId}/{reviewId}")//삭제
    public ResponseEntity<Long> deleteReview(@PathVariable Long reviewId){
        reviewService.remove(reviewId);
        return new ResponseEntity<>(reviewId, HttpStatus.OK);
    }

*/
}
