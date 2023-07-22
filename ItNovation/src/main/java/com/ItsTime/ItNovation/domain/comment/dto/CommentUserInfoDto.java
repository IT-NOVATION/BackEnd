package com.ItsTime.ItNovation.domain.comment.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentUserInfoDto {

    private Long userId;
    private String profileImg;
    private String nickname;


    @Builder
    public CommentUserInfoDto(Long userId, String profileImg, String nickname) {
        this.userId = userId;
        this.profileImg = profileImg;
        this.nickname = nickname;
    }
}
