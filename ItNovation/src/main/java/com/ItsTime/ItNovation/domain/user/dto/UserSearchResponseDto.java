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
    private String userImg;
    private String introduction;
    private List<SearchUserReviewDto> reviews = new ArrayList<>();

    @Builder
    public UserSearchResponseDto(Long userId, String nickName, String userImg, String introduction,
        List<SearchUserReviewDto> reviews) {
        this.userId = userId;
        this.nickName = nickName;
        this.userImg = userImg;
        this.introduction = introduction;
        this.reviews = reviews;
    }
}
