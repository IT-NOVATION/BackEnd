package com.ItsTime.ItNovation.domain.comment.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentWriteRequestDto {
    private Long reviewId;
    private String commentText;
}
