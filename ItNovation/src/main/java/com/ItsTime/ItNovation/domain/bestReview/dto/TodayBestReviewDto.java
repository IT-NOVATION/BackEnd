package com.ItsTime.ItNovation.domain.bestReview.dto;

import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.movie.dto.TodayBestReviewMovieDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TodayBestReviewDto {

    private Long reviewId;
    private String reviewTitle;
    private TodayBestReviewMovieDto movie;


    @Builder
    public TodayBestReviewDto(Long reviewId, String reviewTitle,
        TodayBestReviewMovieDto movie) {
        this.reviewId = reviewId;
        this.reviewTitle = reviewTitle;
        this.movie = movie;
    }
}
