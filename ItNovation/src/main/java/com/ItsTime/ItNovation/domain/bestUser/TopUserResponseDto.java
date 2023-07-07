package com.ItsTime.ItNovation.domain.bestUser;


import com.ItsTime.ItNovation.domain.review.dto.TopUserReviewDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TopUserResponseDto {


    private Long userId;
    private String profileImg;
    private String nickName;
    private String introduction;
    private String grade;
    private int followers;
    private Long followings;



    private List<TopUserReviewDto> reviews;

    @Builder
    public TopUserResponseDto(Long userId, String profileImg, String nickName, String introduction,
        String grade, int followers, Long followings, List<TopUserReviewDto> reviews) {
        this.userId = userId;
        this.profileImg = profileImg;
        this.nickName = nickName;
        this.introduction = introduction;
        this.grade = grade;
        this.followers = followers;
        this.followings = followings;
        this.reviews = reviews;
    }
}
