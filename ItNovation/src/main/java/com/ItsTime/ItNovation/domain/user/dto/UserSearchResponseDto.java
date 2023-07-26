package com.ItsTime.ItNovation.domain.user.dto;


import com.ItsTime.ItNovation.domain.review.dto.SearchUserReviewDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSearchResponseDto {



    private Long userId;
    private String nickName;
    private String profileImg;
    private String introduction;
    private Boolean isMyProfile;
    private Boolean isNowUserFollowThisUser;

    private List<SearchUserReviewDto> reviews = new ArrayList<>();

    @Builder
    public UserSearchResponseDto(Long userId, String nickName, Boolean isMyProfile,Boolean isNowUserFollowThisUser,String userImg, String introduction,
        List<SearchUserReviewDto> reviews) {
        this.userId = userId;
        this.nickName = nickName;
        this.isMyProfile=isMyProfile;
        this.isNowUserFollowThisUser = isNowUserFollowThisUser;
        this.profileImg = userImg;
        this.introduction = introduction;
        this.reviews = reviews;
    }
}
