package com.ItsTime.ItNovation.domain.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewUserInfoDto {

    private Long userId;
    private String bgImg;
    private String nickname;
    private String grade;
    private String introduction;
    private String profileImg;

//    followerNum:Long,
//        "hasReviewLike":Boolean,  추후 해당 기능 추가 후 추가 예정


    @Builder
    public ReviewUserInfoDto(Long userId, String bgImg, String nickname, String grade,
        String introduction, String profileImg) {
        this.userId = userId;
        this.bgImg = bgImg;
        this.nickname = nickname;
        this.grade = grade;
        this.introduction = introduction;
        this.profileImg = profileImg;
    }
}
