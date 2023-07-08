package com.ItsTime.ItNovation.domain.reviewLike;

import com.ItsTime.ItNovation.domain.BaseTimeEntity;
import com.ItsTime.ItNovation.domain.review.Review;
import com.ItsTime.ItNovation.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class ReviewLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewLikeId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewId") // reviewId 필드를 외래키로 매핑
    private Review review;
    private Boolean reviewLike;

    public ReviewLike(Long reviewLikeId, User user, Review review, Boolean reviewLike) {
        this.reviewLikeId = reviewLikeId;
        this.user = user;
        this.review = review;
        this.reviewLike = reviewLike;
    }
}
