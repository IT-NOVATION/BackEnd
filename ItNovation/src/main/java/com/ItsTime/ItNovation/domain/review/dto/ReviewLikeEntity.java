//package com.ItsTime.ItNovation.domain.review.dto;
//
//import com.ItsTime.ItNovation.domain.user.User;
//import com.ItsTime.ItNovation.domain.review.Review;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//@Entity
//@Table(name = "ReviewLike")
//public class ReviewLikeEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "review_like_id")
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "review_id")
//    private Review review;
//
//    @Column(name = "review_like")
//    private Boolean reviewLike;
//
//    public ReviewLikeEntity() {
//        // 기본 생성자
//    }
//
//    public ReviewLikeEntity(User user, Review review, Boolean reviewLike) {
//        this.user = user;
//        this.review = review;
//        this.reviewLike = reviewLike;
//    }
//
//}
