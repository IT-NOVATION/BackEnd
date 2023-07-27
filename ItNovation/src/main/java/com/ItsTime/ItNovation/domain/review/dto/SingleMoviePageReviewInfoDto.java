package com.ItsTime.ItNovation.domain.review.dto;


import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SingleMoviePageReviewInfoDto {

    private Long reviewId;
    private Boolean hasSpoiler;
    private String reviewTitle;
    private String reviewMainText;
    private LocalDate createdDate;
    private Float starScore;
    private int reviewLikeCount;
    private int commentCount;


    @Builder
    public SingleMoviePageReviewInfoDto(Long reviewId, Boolean hasSpoiler, String reviewTitle,
        String reviewMainText, LocalDate createdDate, Float starScore, Integer reviewLikeCount, int commentCount) {
        this.reviewId = reviewId;
        this.hasSpoiler = hasSpoiler;
        this.reviewTitle = reviewTitle;
        this.reviewMainText = reviewMainText;
        this.createdDate = createdDate;
        this.starScore = starScore;
        this.commentCount=commentCount;
        this.reviewLikeCount = reviewLikeCount;
    }
}
