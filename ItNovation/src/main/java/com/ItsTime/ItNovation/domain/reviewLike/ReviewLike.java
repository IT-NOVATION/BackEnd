package com.ItsTime.ItNovation.domain.reviewLike;

import com.ItsTime.ItNovation.domain.review.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewLikeId;
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "reviewId") // reviewId 필드를 외래키로 매핑
    private Review review;
    private Boolean reviewLike;

    public ReviewLike(Long reviewLikeId, Long userId, Review review, Boolean reviewLike) {
        this.reviewLikeId = reviewLikeId;
        this.userId = userId;
        this.review = review;
        this.reviewLike = reviewLike;
    }
}
