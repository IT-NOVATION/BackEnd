package com.ItsTime.ItNovation.domain.comment.dto;


import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentReadDto {

    private Long commentId;
    private String commentText;
    private String createDate;
    private CommentUserInfoDto commentUserInfo;


    @Builder
    public CommentReadDto(Long commentId, String commentText, String createDate, CommentUserInfoDto commentUserInfo) {
        this.commentId = commentId;
        this.commentText = commentText;
        this.createDate = createDate;
        this.commentUserInfo = commentUserInfo;
    }
}
