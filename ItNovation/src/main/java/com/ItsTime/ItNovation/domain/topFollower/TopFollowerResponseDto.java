package com.ItsTime.ItNovation.domain.topFollower;

import com.ItsTime.ItNovation.domain.review.dto.TopFollowerReviewResponseDto;
import com.ItsTime.ItNovation.domain.review.dto.TopUserReviewDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class TopFollowerResponseDto {
    private Long userId;
    private String profileImg;
    private String nickName;
    private String introduction;
    private List<TopFollowerReviewResponseDto> reviews;

    @Builder
    public TopFollowerResponseDto(Long userId, String profileImg, String nickName, String introduction, List<TopFollowerReviewResponseDto> reviews) {
        this.userId = userId;
        this.profileImg = profileImg;
        this.nickName = nickName;
        this.introduction = introduction;
        this.reviews = reviews;
    }
}
