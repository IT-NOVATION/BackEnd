package com.ItsTime.ItNovation.domain.review.dto;

import com.ItsTime.ItNovation.domain.movie.dto.TopUserMovieDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TopUserReviewDto {


    private Long reviewId;
    private String reviewTitle;
    private Float star;
    private String reviewMainText;
    private String createdDate;
    private int reviewLikeCount;

    private TopUserMovieDto movie;
    private Boolean hasSpoiler;


    @Builder
    public TopUserReviewDto(Long reviewId, String reviewTitle, Float star, String reviewMainText,
        String createdDate, int reviewLikeCount, TopUserMovieDto movie, Boolean hasSpoiler) {
        this.reviewId = reviewId;
        this.reviewTitle = reviewTitle;
        this.star = star;
        this.reviewMainText = reviewMainText;
        this.createdDate = createdDate;
        this.reviewLikeCount = reviewLikeCount;
        this.movie = movie;
        this.hasSpoiler= hasSpoiler;
    }
}
