package com.ItsTime.ItNovation.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    //리뷰 아이디
    private Long reviewId;
    //리뷰 작성할 사용자
    private Long userId;
    //리뷰 작성할 영화
    private Long movieId;
    //리뷰 좋아요 받는 아이디
    private Long reviewLikeId;
    //리뷰 제목
    private String reviewTitle;
    //리뷰 본문
    private String reviewMainText;
    //어떤 점이 좋았는지{제작, 배우의 연기, 스토리, 음악, 연출}
    private Boolean production;
    private Boolean actor;
    private Boolean director;
    private Boolean music;
    private Boolean immersion;
    //날짜 체크 여부
    private Boolean isCheckDate;
    //스포일러 여부
    private Boolean spoiler;
    //감상한 날짜
    private LocalDateTime watchDate;
    //리뷰 작성한 날짜
    private LocalDateTime createdDate;

}
