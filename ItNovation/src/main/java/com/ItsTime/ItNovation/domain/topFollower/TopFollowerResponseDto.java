package com.ItsTime.ItNovation.domain.topFollower;

import com.ItsTime.ItNovation.domain.review.dto.TopFollowerReviewResponseDto;
import com.ItsTime.ItNovation.domain.review.dto.TopUserReviewDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TopFollowerResponseDto {
    private Long userId;
    private Boolean isMyProfile;
    private Boolean loginUserPushedFollow;
    private String profileImg;
    private String nickName;
    private String introduction;
    private List<TopFollowerReviewResponseDto> reviews;

    @Builder
    public TopFollowerResponseDto(Long userId, Boolean isMyProfile, Boolean loginUserPushedFollow, String profileImg, String nickName, String introduction, List<TopFollowerReviewResponseDto> reviews) {
        this.userId = userId;
        this.isMyProfile = isMyProfile;
        this.loginUserPushedFollow = loginUserPushedFollow;
        this.profileImg = profileImg;
        this.nickName = nickName;
        this.introduction = introduction;
        this.reviews = reviews;
    }

}
