package com.ItsTime.ItNovation.service.movielog;

import com.ItsTime.ItNovation.domain.reviewLike.ReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewLikeService {
    private final ReviewLikeRepository reviewLikeRepository;

    /**
     *
     * @param reviewId
     * @return 리뷰의 좋아요 수 가져오기
     */
    @Transactional
    public int countReviewLikebyReview(long reviewId) {
       int count= reviewLikeRepository.countReviewLikeByReviewId(reviewId);

       return count;
    }
}
