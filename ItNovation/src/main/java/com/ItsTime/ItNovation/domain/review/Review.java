
package com.ItsTime.ItNovation.domain.review;


import com.ItsTime.ItNovation.domain.BaseTimeEntity;
import com.ItsTime.ItNovation.domain.movie.Movie;
import com.ItsTime.ItNovation.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"movie", "user"})
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /*ReviewLikeEntity 생성할 것
    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewLike reviewLike;
    */

    //평가 내용
    private Boolean production;

    private Boolean actor;

    private Boolean director;

    private Boolean music;

    private Boolean immersion;

    private Boolean is_check_date;
    //스포일러 여부
    private Boolean spoiler;
    //리뷰 작성 하는 부분
    private Long reviewTitle;

    private Long reviewMainText;

    //리뷰 제목 수정
    public void changeTitle(Long reviewTitle){
        this.reviewTitle = reviewTitle;
    }
    //리뷰 본문 수정
    public void changeText(Long reviewMainText){
        this.reviewMainText = reviewMainText;
    }

}
