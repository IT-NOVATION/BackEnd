package com.ItsTime.ItNovation.domain.user.dto;


import com.ItsTime.ItNovation.domain.user.Grade;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewUserInfoDto {

    private Long userId;
    private String bgImg;
    private String nickname;
    private Grade grade;
    private String introduction;
    private String profileImg;
    private Long folllowerNum;
    private Long followingNum;
    private Boolean hasReviewLike;

//    followerNum:Long,
//        "hasReviewLike":Boolean,  추후 해당 기능 추가 후 추가 예정


    @Builder
    public ReviewUserInfoDto(Long userId, String bgImg, String nickname, Grade grade,
        String introduction, String profileImg, Long folllowerNum, Long followingNum, Boolean hasReviewLike) {
        this.userId = userId;
        this.bgImg = bgImg;
        this.nickname = nickname;
        this.grade = grade;
        this.introduction = introduction;
        this.profileImg = profileImg;
        this.folllowerNum = folllowerNum;
        this.followingNum = followingNum;
        this.hasReviewLike=hasReviewLike;
    }
}
