package com.ItsTime.ItNovation.domain.comment.dto;


import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentReadResponseDto {


    private int lastPage;
    private int firstPage;
    private int nowPage;
    private int totalCommentCount;
    private List<CommentReadDto> commentList;


    @Builder
    public CommentReadResponseDto(int lastPage, int firstPage, int nowPage, int totalCommentCount,
        List<CommentReadDto> commentList) {
        this.lastPage = lastPage;
        this.firstPage = firstPage;
        this.nowPage = nowPage;
        this.totalCommentCount = totalCommentCount;
        this.commentList = commentList;
    }
}
