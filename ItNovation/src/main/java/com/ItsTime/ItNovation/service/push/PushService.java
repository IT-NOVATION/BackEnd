package com.ItsTime.ItNovation.service.push;


import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.review.ReviewRepository;
import com.ItsTime.ItNovation.domain.review.dto.PushReviewLikeResponseDto;
import com.ItsTime.ItNovation.domain.review.dto.PushReviewLikeResponseDto.PushReviewLikeResponseDtoBuilder;
import com.ItsTime.ItNovation.domain.reviewLike.ReviewLike;
import com.ItsTime.ItNovation.domain.reviewLike.ReviewLikeRepository;
import com.ItsTime.ItNovation.domain.user.User;
import com.ItsTime.ItNovation.domain.user.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PushService {
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    @Transactional
    public ResponseEntity pushReviewLike(Long reviewId, Long pushUserId) {
        try {
            Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰가 없어요!"));
            User user = userRepository.findById(pushUserId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없어요!"));

            Optional<ReviewLike> reviewLike = reviewLikeRepository.findReviewLikeByReviewIdAndUserId(
                pushUserId, reviewId);
            if(reviewLike.isEmpty()){
                ReviewLike build = ReviewLike.builder()
                    .user(user)
                    .review(review)
                    .reviewLike(true)
                    .build();
                reviewLikeRepository.save(build);
                PushReviewLikeResponseDto reviewLikeDto = PushReviewLikeResponseDto.builder()
                    .isReviewLike(build.getReviewLike())
                    .build();
                return ResponseEntity.status(200).body(reviewLikeDto);

            }
            else{
                ReviewLike presentReviewLike = reviewLike.get();
                presentReviewLike.updateReviewLike();
                //reviewLikeRepository.save(presentReviewLike);
                PushReviewLikeResponseDto reviewLikeDto = PushReviewLikeResponseDto.builder()
                    .isReviewLike(presentReviewLike.getReviewLike())
                    .build();
                System.out.println("reviewLikeDto = " + reviewLikeDto);
                return ResponseEntity.status(200).body(reviewLikeDto);
            }
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
