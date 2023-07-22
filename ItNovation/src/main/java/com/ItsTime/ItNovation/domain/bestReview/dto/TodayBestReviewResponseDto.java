package com.ItsTime.ItNovation.domain.bestReview.dto;

import com.ItsTime.ItNovation.domain.review.Review;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TodayBestReviewResponseDto {

    private Long userId;
    private String profileImg;
    private Boolean isMyProfile;
    private Boolean isNowUserFollowThisUser;
    private String nickName;
    private String introduction;
    private List<TodayBestReviewDto> reviews = new ArrayList<>();


    @Builder
    public TodayBestReviewResponseDto(Long userId, String profileImg, String nickName,Boolean isMyProfile,Boolean isNowUserFollowThisUser,
        String introduction, List<TodayBestReviewDto> reviews) {
        this.userId = userId;
        this.profileImg = profileImg;
        this.isMyProfile = isMyProfile;
        this.isNowUserFollowThisUser = isNowUserFollowThisUser;
        this.nickName = nickName;
        this.introduction = introduction;
        this.reviews = reviews;
    }
}
