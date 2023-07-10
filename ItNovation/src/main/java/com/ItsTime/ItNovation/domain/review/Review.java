
package com.ItsTime.ItNovation.domain.review;


import com.ItsTime.ItNovation.domain.BaseTimeEntity;
import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.reviewLike.ReviewLike;
import com.ItsTime.ItNovation.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    private Float star;

    private String reviewTitle;


    @Column(columnDefinition = "LONGTEXT")
    private String reviewMainText;

    private Boolean hasGoodStory;

    private Boolean hasGoodProduction;

    private Boolean hasGoodScenario;

    private Boolean hasGoodDirecting;

    private Boolean hasGoodOst;

    private Boolean hasGoodVisual;

    private Boolean hasGoodActing;
    private Boolean hasGoodCharterCharming;
    private Boolean hasGoodDiction;
    private Boolean hasCheckDate;
    private Boolean hasSpoiler;

    private String watchDate;

    @ManyToOne(fetch = FetchType.LAZY)
    // 현재 엔티티(Review)의 테이블에 조인 컬럼(foreign key)을 생성한다는 의미
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //비식별 관계 사용 - 다쪽 엔티티가 일쪽 엔티티의 외래키를 가지는 형태 -> 서로 독립적, 유연
    @OneToMany(mappedBy = "review")
    private List<ReviewLike> reviewLikes;


    @Builder
    public Review( Float star, String reviewTitle, String reviewMainText, Boolean hasGoodStory, Boolean hasGoodProduction, Boolean hasGoodScenario, Boolean hasGoodDirecting, Boolean hasGoodOst, Boolean hasGoodVisual, Boolean hasGoodActing, Boolean hasGoodCharterCharming, Boolean hasGoodDiction, Boolean hasCheckDate, Boolean hasSpoiler, String watchDate, Movie movie, User user, List<ReviewLike> reviewLikes) {

        this.star = star;
        this.reviewTitle = reviewTitle;
        this.reviewMainText = reviewMainText;
        this.hasGoodStory = hasGoodStory;
        this.hasGoodProduction = hasGoodProduction;
        this.hasGoodScenario = hasGoodScenario;
        this.hasGoodDirecting = hasGoodDirecting;
        this.hasGoodOst = hasGoodOst;
        this.hasGoodVisual = hasGoodVisual;
        this.hasGoodActing = hasGoodActing;
        this.hasGoodCharterCharming = hasGoodCharterCharming;
        this.hasGoodDiction = hasGoodDiction;
        this.hasCheckDate = hasCheckDate;
        this.hasSpoiler = hasSpoiler;
        this.watchDate = watchDate;
        this.movie = movie;
        this.user = user;
        this.reviewLikes = reviewLikes;
    }


}
