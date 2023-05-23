package com.ItsTime.ItNovation.service.review;

import com.ItsTime.ItNovation.domain.review.dto.ReviewDTO;
import java.util.List;

public interface ReviewService {

    List<ReviewDTO> getListOfMovie(Long movieId);

    Long register(ReviewDTO movieReviewDTO);

    void modify(ReviewDTO movieReviewDTO);

    void remove(Long reviewId);

    //ReviewDTO 객체를 Review Entity 객체로 변환
    //필요한것
    /* 무비 빌더로 무비 아이디, 유저 아이디 갖고 오고싶은데 존재여부
    default Review dtoToEntity(ReviewDTO movieDTO){
        Review movieReview = Review.builder()
                .movie(Movie.builder().movieId(ReviewDTO.getMovieId.getMovieId().build))

                .build();
        return movieReview;
    }
    */
    //해당 리뷰에 대한 정보 갖고있는 DTO
    /*
    default ReviewDTO entityToDto(Review movieReview){

    }
     */
}
